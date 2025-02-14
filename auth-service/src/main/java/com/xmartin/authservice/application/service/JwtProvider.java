package com.xmartin.authservice.application.service;

import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.RouteValidatorPort;
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

    private final RouteValidatorPort routeValidatorPort;

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

    public boolean validate(String token, RequestModel requestModel) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        if (isTokenExpired(token) || !isAdmin(token) && routeValidatorPort.isAdminPath(requestModel)) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validateOnlyToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return !isTokenExpired(token);

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
