package com.example.topgoback.Users.DTO;

public class ResetPasswordDTO {
    String code;
    String new_password;

    public ResetPasswordDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
