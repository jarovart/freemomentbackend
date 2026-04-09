/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Artem
 */
@Service
public class JwtService {

    private static final long VALIDITY_MS = 1000L * 60 * 60 * 24; // 24h
    private final Key key = Keys.hmacShaKeyFor("change_this_to_a_long_random_secret_key_please!".getBytes());

    public String generateToken(String username, Map<String, Object> extraClaims) {
        Date now = new Date();
        return Jwts.builder()
                   .setClaims(extraClaims)
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + VALIDITY_MS))
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean isTokenValid(String token, String username) {
        try {
            String sub = parseToken(token).getBody().getSubject();
            return sub.equals(username) && parseToken(token).getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
