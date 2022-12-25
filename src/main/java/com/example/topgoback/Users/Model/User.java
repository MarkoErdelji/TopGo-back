package com.example.topgoback.Users.Model;

import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;


import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Table(name="users")
@Inheritance(strategy=TABLE_PER_CLASS)
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "mySeqGenUser", sequenceName = "mySeqGenUser", initialValue = 7, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenUser")
    @Column(name="id")
    private Integer id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;


    @Column(name = "profilePicture", nullable = true, length = 500000)
    private String profilePicture;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "isBlocked", nullable = true)
    private boolean isBlocked;

    @Column(name="userType",nullable = true)
    private UserType userType;


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
    }

    public User(String firstName, String lastName, String profilePicture, String email, String password, String phoneNumber, String address,UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.email = email;
        this.profilePicture = profilePicture;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userType = userType;
        this.isBlocked = false;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.getUserType().toString()));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
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



}
