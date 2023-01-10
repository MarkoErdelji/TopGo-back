package com.example.topgoback.Users.DTO;

import com.example.topgoback.Users.Model.Driver;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class DriverInfoDTO {
    /*{
  "id": 123,
  "name": "Pera",
  "surname": "Perić",
  "profilePicture": "U3dhZ2dlciByb2Nrcw==",
  "telephoneNumber": "+381123123",
  "email": "pera.peric@email.com",
  "address": "Bulevar Oslobodjenja 74"
}*/

    @NotNull(message = "is required!")
    private Integer id;
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


    public DriverInfoDTO() {
    }

    public DriverInfoDTO(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getFirstName();
        this.surname = driver.getLastName();
        this.profilePicture = driver.getProfilePicture();
        this.telephoneNumber = driver.getPhoneNumber();
        this.email = driver.getEmail();
        this.address = driver.getAddress();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
