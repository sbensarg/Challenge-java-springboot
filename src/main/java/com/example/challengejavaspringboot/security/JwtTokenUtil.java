package com.example.challengejavaspringboot.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration}")
    private String EXPIRE_DURATION;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    public String generateAccessToken(JwtUserDetails user) {
        return Jwts.builder()
                .setSubject(String.format("%s", user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(EXPIRE_DURATION)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }


    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
