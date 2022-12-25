package com.example.topgoback.ProfileChangesRequest.Model;

import com.example.topgoback.Users.Model.Driver;
import jakarta.persistence.*;

@Entity
public class ProfileChangesRequest {
    @Id
    @SequenceGenerator(name = "mySeqGenV1Req", sequenceName = "mySeqV1Req", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1Req")
    @Column(name="id")
    private Long id;
    
    @ManyToOne
    private Driver driver;
    private String firstName;
    private String lastName;
    private String email;

    public ProfileChangesRequest() {
    }

    public ProfileChangesRequest(Long id, Driver driver, String firstName, String lastName, String email, String phoneNumber, String address, String profilePicture) {
        this.id = id;
        this.driver = driver;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profilePicture = profilePicture;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    private String phoneNumber;
    private String address;
    @Column(length = 500000)
    private String profilePicture;



    public String getFirstName() {
        return firstName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
