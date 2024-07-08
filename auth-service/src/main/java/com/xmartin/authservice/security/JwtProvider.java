package com.xmartin.authservice.security;

import com.xmartin.authservice.controller.dto.RequestDto;
import com.xmartin.authservice.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationms}")
    private Long expirationms;

    private final RouteValidator routeValidator;

    public String createToken(UserModel userModel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userModel.getId());
        claims.put("role", userModel.getRole());

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userModel.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationms))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token, RequestDto requestDto) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        if (isTokenExpired(token) || !isAdmin(token) && routeValidator.isAdminPath(requestDto)) {
            return false;
        } else {
            return true;
        }

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isAdmin(String token) {
        return extractAllClaims(token)
                .get("role")
                .equals("ROLE_ADMIN");
    }

    public String getEmailFromToken(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (Exception e) {
            return "bad token";
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
