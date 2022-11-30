package com.example.topgoback.Users.Controller;

import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> saveTeacher(@RequestBody User user) {

        userService.addOne(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }




}
