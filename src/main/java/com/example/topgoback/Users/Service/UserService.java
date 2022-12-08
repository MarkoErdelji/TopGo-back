package com.example.topgoback.Users.Service;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import com.example.topgoback.Users.DTO.UserListDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
}
