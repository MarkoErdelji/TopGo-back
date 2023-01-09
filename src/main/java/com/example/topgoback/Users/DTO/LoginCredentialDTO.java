package com.example.topgoback.Users.DTO;

import javax.validation.constraints.NotBlank;

public class LoginCredentialDTO {

    @NotBlank(message= "{format}")
    private String email;

    @NotBlank(message= "{format}")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
