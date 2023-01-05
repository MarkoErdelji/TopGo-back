package com.example.topgoback.Users.Service;

import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialNotFoundException;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not findUser with email = " + loginCredentialDTO.getEmail());
        boolean isPasswordMatching = passwordEncoder.matches(loginCredentialDTO.getPassword(),userRes.getPassword());
        if(!isPasswordMatching) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong password");
        }
        if(userRes.isBlocked()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User is blocked!");
        }
        final String token = jwtTokenUtil.generateToken(userRes);

        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        jwtTokenDTO.setAccessToken(token);
        jwtTokenDTO.setRefreshToken(token);

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


    public void updateOne(User user) {
        userRepository.save(user);
    }
}
