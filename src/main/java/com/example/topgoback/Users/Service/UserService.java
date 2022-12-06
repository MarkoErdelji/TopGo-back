package com.example.topgoback.Users.Service;

import com.example.topgoback.Users.DTO.CreateUserDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        List<User> users =  userRepository.findAll();
        if(users.isEmpty()){
        return null;}
        else{
            return users;
        }
    }

    public User addOne(CreateUserDTO userDTO) {
        User u = new User(userDTO);
        userRepository.save(u);
        return u;}

}
