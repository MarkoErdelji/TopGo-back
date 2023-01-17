package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ResetPasswordDTO {

    @NotNull(message = "is required!")
    String code;

    @NotNull(message = "is required!")
    @Length(min = 6,message = "cannot be shorter than 6 characters!")
    String newPassword;

    public ResetPasswordDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
