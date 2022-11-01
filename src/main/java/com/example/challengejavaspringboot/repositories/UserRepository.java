package com.example.challengejavaspringboot.repositories;

import com.example.challengejavaspringboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = :#{#req. email} or u.username = :#{#req.username}")
    Optional<User> findUserByEmailorUsername(@Param("req") User req);
    Optional<User> findJwtUserByUsername(String username);
    Optional<User> findJwtUserByEmail(String email);

}