package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateDriverDTO {

    @NotNull(message = "is required!")
    private String name;
    @NotNull(message = "is required!")
    private String surname;
    @NotNull(message = "is required!")
    private String profilePicture;
    @NotNull(message = "is required!")
    private String telephoneNumber;
    @NotNull(message = "is required!")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",message="format is not valid!")
    private String email;
    @NotNull(message = "is required!")
    private String address;
    @NotNull(message = "is required!")
    @Length(min = 6, message = "Cannot be less than 6 characters!")
    private String password;

    public CreateDriverDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public CreateDriverDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
