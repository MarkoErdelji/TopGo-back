package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class ChangePasswordDTO {
    @NotNull(message = "is required!")
    @Size(min = 6,message = "cannot be shorter than 6 characters!")
    private String newPassword;
    @NotNull(message = "is required!")
    private String oldPassword;

    public ChangePasswordDTO() {
    }


    public ChangePasswordDTO(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
