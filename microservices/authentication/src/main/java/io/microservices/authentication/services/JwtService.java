package io.microservices.authentication.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.microservices.authentication.model.User;

@Service
public class JwtService {

    @Value("${auth.secret}")
    private String SECRET;

    public boolean validateToken(final String token) {
        return true;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("sub", user.getId());
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims){
        return Jwts.builder().
        claims(claims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
        .signWith(getSigninKey())
        .compact();
    }

    private Key getSigninKey(){
        System.out.println(SECRET);
        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }
}
