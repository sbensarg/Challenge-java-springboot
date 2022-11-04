package com.example.challengejavaspringboot.services;

import com.example.challengejavaspringboot.entity.User;
import com.example.challengejavaspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class JwtUserService {

    private final UserRepository jwtUserRepository;

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
        return ret.orElseThrow(()-> new EntityNotFoundException("User not found by username|email!"));
    }

}
