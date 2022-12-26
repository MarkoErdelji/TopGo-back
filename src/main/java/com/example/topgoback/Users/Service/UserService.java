package com.example.topgoback.Users.Service;

import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserListDTO findAll() {

        UserListDTO userListDTo = new UserListDTO();
        userListDTo.setTotalCount(243);
        ArrayList<UserListResponseDTO> userListResponseDTOS = new ArrayList<>();
        userListResponseDTOS.add(UserListResponseDTO.getMockupData());
        userListDTo.setResults(userListResponseDTOS);
        if(userListDTo.getResults().isEmpty()){
        return null;}
        else{
            return userListDTo;
        }
    }

    public User findOne(int id){
        return userRepository.findById(id).orElse(null);
    }


    public User addOne(CreateUserDTO userDTO) {
        User u = new User(userDTO);
        userRepository.save(u);
        return u;}


    public void blockUser(User user) {
        user.setBlocked(true);

        userRepository.save(user);
    }

    public void unblockUser(User user) {
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

    public User login(LoginCredentialDTO loginCredentialDTO) throws CredentialNotFoundException,UsernameNotFoundException {
        User userRes = userRepository.findByEmail(loginCredentialDTO.getEmail());
        if(userRes == null)
            throw new UsernameNotFoundException("Could not findUser with email = " + loginCredentialDTO.getEmail());
        boolean isPasswordMatching = passwordEncoder.matches(loginCredentialDTO.getPassword(),userRes.getPassword());
        if(!isPasswordMatching) {
            throw new CredentialNotFoundException("Wrong password!");
        }
        return userRes;
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
