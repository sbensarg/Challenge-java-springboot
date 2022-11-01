package com.example.challengejavaspringboot.security;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.services.JwtUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final JwtUserService jwtUserService;

    public JwtUserDetailsService(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = jwtUserService.getJwtUserByUsernameorEmail(username);

        return new JwtUserDetails(user);
    }
}
