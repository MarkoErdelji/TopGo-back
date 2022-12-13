package com.example.topgoback.Users.DTO;

import com.example.topgoback.Enums.UserType;

public class UserTokenDTO {
    private String email;

    private UserType userType;


    public UserTokenDTO(String email, UserType userType) {
        this.email = email;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
