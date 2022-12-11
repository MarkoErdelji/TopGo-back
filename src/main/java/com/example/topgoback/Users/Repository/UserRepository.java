package com.example.topgoback.Users.Repository;

import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

}
