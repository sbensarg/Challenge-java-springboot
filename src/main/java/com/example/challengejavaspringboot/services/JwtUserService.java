package com.example.challengejavaspringboot.services;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class JwtUserService {

    private final UserRepository jwtUserRepository;


    public Optional<User> findJwtUserByEmail(String email) {
        return jwtUserRepository.findJwtUserByEmail(email);
    }

    public User getJwtUserByUsername(String username) {
        return jwtUserRepository.findJwtUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found by username!"));
    }

    public User getJwtUserByUsernameorEmail(String username) {
        Optional<User> ret;
        ret = jwtUserRepository.findJwtUserByUsername(username);
        if (!ret.isPresent()) {
            ret = jwtUserRepository.findJwtUserByEmail(username);
        }
        System.out.println(ret);
        return ret.orElseThrow(()-> new EntityNotFoundException("User not found by username|email!"));
    }

}
