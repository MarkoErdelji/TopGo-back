package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UpdatePassengerDTO {

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

    public UpdatePassengerDTO() {
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
}
