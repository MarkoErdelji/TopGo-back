package com.example.topgoback.Users.Repository;

import com.example.topgoback.Users.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
