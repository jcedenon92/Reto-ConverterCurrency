package com.jcedenon.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//S4
@Component
public class JwtUtil implements Serializable {

    @Value("${jjwt.secret}")
    private String secret;

    @Value("${jjwt.expiration}")
    private String expirationTime;

    /**
     * Genera el token
     * @param user
     * @return
     */
    public String generateToken(UserSecurity user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("username", user.getUsername());
        claims.put("test", "value-demo");

        return doGenerateToken(claims, user.getUsername());
    }

    /**
     * Genera el token con los claims y el username del usuario que se le pasa por parametro
     * @param claims
     * @param username
     * @return
     */
    public String doGenerateToken(Map<String, Object> claims, String username){
        Long expirationTimeLong = Long.parseLong(expirationTime);

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 2000); //aprox 46min

        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /**
     * Obtiene todos los claims del token
     * @param token
     * @return
     */
    public Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene el username del token que se le pasa por parametro.
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Obtiene la fecha de expiracion del token que se le pasa por parametro.
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * Valida si el token esta expirado o no.
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Valida si el token es valido o no.
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
