package com.example.challengejavaspringboot.repositories;

import com.example.challengejavaspringboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
