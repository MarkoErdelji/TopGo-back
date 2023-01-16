package com.example.topgoback.Users.Service;

import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.PasswordResetTokens.Repository.PasswordResetTokenRepository;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialNotFoundException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    UserService(JavaMailSender mailSender){this.mailSender = mailSender;}
    public UserListDTO findAll(Pageable pageable) {

        Page<User> page = userRepository.findAll(pageable);
        List<UserListResponseDTO> userListResponseDTOS = UserListResponseDTO.convertToUserListResponseDTO(page.getContent());

        UserListDTO users = new UserListDTO(new PageImpl<>(userListResponseDTOS, pageable, page.getTotalElements()));
        return users;
    }

    public User findOne(int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!");
        }
    }


    public User addOne(CreateUserDTO userDTO) {
        User u = new User(userDTO);
        userRepository.save(u);
        return u;}


    public void blockUser(int userId) {
        User user = findOne(userId);
        if(user.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already blocked!");
        }
        user.setBlocked(true);
        userRepository.save(user);
    }

    public void unblockUser(int userId) {
        User user = findOne(userId);
        if(!user.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User is not blocked!");
        }
        user.setBlocked(false);
        userRepository.save(user);
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        User userRes = userRepository.findByEmail(username);
        if(userRes == null)
            throw new UsernameNotFoundException("Could not findUser with email = " + username);
        return userRes;
    }

    public JWTTokenDTO login(LoginCredentialDTO loginCredentialDTO) throws ResponseStatusException {
        User userRes = userRepository.findByEmail(loginCredentialDTO.getEmail());
        if(userRes == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong username or password!");
        boolean isPasswordMatching = passwordEncoder.matches(loginCredentialDTO.getPassword(),userRes.getPassword());
        if(!isPasswordMatching) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong username or password!");
        }
        if(userRes.isBlocked()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong username or password!");
        }
        final String token = jwtTokenUtil.generateToken(userRes);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userRes);

        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        jwtTokenDTO.setAccessToken(token);
        jwtTokenDTO.setRefreshToken(refreshToken);

        return jwtTokenDTO;
    }

    public void changeUserPassword(int userId,ChangePasswordDTO changePasswordDTO) {
        User user = findOne(userId);
        if (!passwordEncoder.matches(changePasswordDTO.getOld_password(),user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Current password is not matching!");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNew_password()));
        userRepository.save(user);
    }

    public void updateUserPassword(User user,String NewPassword) throws CredentialNotFoundException {
        user.setPassword(passwordEncoder.encode(NewPassword));
        userRepository.save(user);
    }

    public UserRef loadUserReferenceByUsername(String username){
        User userRes = userRepository.findByEmail(username);
        if(userRes == null)
            throw new UsernameNotFoundException("Could not findUser with email = " + username);
        UserRef userRef = new UserRef();
        userRef.setId(userRes.getId());
        userRef.setEmail(userRes.getEmail());
        return userRef;

    }


    public void sendEmail(int userId) throws IOException, MessagingException {
        User user = findOne(userId);
        Path filePath = Paths.get("src/main/resources/ResetPasswordEmail.html");
        String emailTemplate = Files.readString(filePath, StandardCharsets.UTF_8);
        String uniqueToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpirationTime(LocalDateTime.now().plusHours(24));
        passwordResetToken.setToken(uniqueToken);
        tokenRepository.save(passwordResetToken);
        emailTemplate = emailTemplate.replace("{{action_url}}","http://localhost:4200/login/resetPassword?token="+uniqueToken+"&id=" + user.getId());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("topGoAppRS@gmail.com");
        helper.setText(emailTemplate,true);
        mailSender.send(message);
    }
    public void updateOne(User user) {
        userRepository.save(user);
    }

    public void resetUserPassword(int userId,ResetPasswordDTO resetPasswordDTO) {
        User user = findOne(userId);
        String code = resetPasswordDTO.getCode();
        Optional<PasswordResetToken> passwordResetToken = tokenRepository.findOneByToken(code);
        if(passwordResetToken.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Code is expired or not correct!");
        }
        if(passwordResetToken.get().getExpirationTime().isBefore(LocalDateTime.now())){
            tokenRepository.delete(passwordResetToken.get());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Code is expired or not correct!");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNew_password()));
        userRepository.save(user);
    }

    public JWTTokenDTO refreshToken(JWTTokenDTO jwtTokenDTO) {

        if(jwtTokenUtil.isTokenExpired(jwtTokenDTO.getRefreshToken())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Your refresh token has expired,please log in again!");
        }
        User user = userRepository.findByEmail(jwtTokenUtil.getUsernameFromToken(jwtTokenDTO.getRefreshToken()));

        String newJWT = jwtTokenUtil.generateToken(user);
        String newRefresh = jwtTokenUtil.generateRefreshToken(user);

        JWTTokenDTO newTokenDto = new JWTTokenDTO();

        newTokenDto.setAccessToken(newJWT);
        newTokenDto.setRefreshToken(newRefresh);
        return newTokenDto;
    }

    public UserListResponseDTO getOne(Integer id) {
        Optional<User> user  = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!");
        }
        UserListResponseDTO userDto = new UserListResponseDTO();
        userDto.setId(user.get().getId());
        userDto.setAddress(user.get().getAddress());
        userDto.setEmail(user.get().getEmail());
        userDto.setName(user.get().getFirstName());
        userDto.setProfilePicture(user.get().getProfilePicture());
        userDto.setSurname(user.get().getLastName());
        userDto.setTelephoneNumber(user.get().getPhoneNumber());
        return (userDto);
    }
}
