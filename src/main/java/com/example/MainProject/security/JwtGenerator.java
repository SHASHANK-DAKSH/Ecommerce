package com.example.MainProject.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiration = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET)
                .compact();

        return token;
    }

    public String generateTokenByEmail(String email) {
        Date currentDate = new Date();
        Date expiration = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET)
                .compact();

        return token;
    }

    public String getUserNameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SECRET)
                .parseClaimsJws(token)// changed from jwt
                .getBody();


        return claims.getSubject();
    }

    public Date getExpiration(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SECRET)
                .parseClaimsJws(token)// changed from jwt
                .getBody();


        return claims.getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstant.JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was  Expired");
        }catch (SignatureException e){
            throw new AuthenticationCredentialsNotFoundException("Jwt was Incorrect");
        }
    }
}
