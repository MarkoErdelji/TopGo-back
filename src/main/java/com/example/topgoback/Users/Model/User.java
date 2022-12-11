package com.example.topgoback.Users.Model;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Table(name="users")
@Inheritance(strategy=TABLE_PER_CLASS)
public class User {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    @Column(name="id")
    private Integer id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "profilePicture", nullable = true)
    private String profilePicture;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;
    @Column(name = "isBlocked", nullable = false)
    private boolean isBlocked;


    public User(){};
    public User(CreateUserDTO userDTO){
        this.firstName = userDTO.getName();
        this.lastName = userDTO.getSurname();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.profilePicture = userDTO.getProfilePicture();
        this.phoneNumber = userDTO.getTelephoneNumber();
        this.address = userDTO.getAddress();
        this.isBlocked = false;
        this.isActive = false;
    }

    public User(String firstName, String lastName, String profilePicture, String email, String password, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.email = email;
        this.profilePicture = profilePicture;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isActive = true;
        this.isBlocked = false;
        this.isActive = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setActive(boolean active) {
        isActive = active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isActive() {
        return isActive;
    }


}
