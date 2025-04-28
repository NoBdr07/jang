package com.bdr.jang.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${bdr.jang.jwtSecret}")
    private String jwtSecret;

    @Value("${bdr.jang.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.httpOnly}")
    private boolean cookieHttpOnly;

    @Value("${cookie.sameSite}")
    private boolean sameSite;

    /**
     * Key generated from jwt secret
     */
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Method to generate a token containing the username
     * @param userId id of user
     * @return a string container the generated token
     */
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Method that checks if token is valid
     * @param token jwt token to control
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Expired token");
        } catch (Exception e) {
            System.err.println("Invalid token");
        }
        return false;
    }


    /**
     * Method to get username from a token
     * @param token jwt
     * @return the username of connected user
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Method that create a secure cookie
     * @param name cookie name
     * @param value cookie value
     * @param maxAge cookie duration
     * @param secure is secure or not
     * @return a Cookie
     */
    public Cookie createCookie(String name, String value, int maxAge, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(cookieHttpOnly);
        cookie.setSecure(cookieSecure);
        if(sameSite) {
            cookie.setAttribute("SameSite", "None");
        }
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
