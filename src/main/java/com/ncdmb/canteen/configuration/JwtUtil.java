package com.ncdmb.canteen.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final Key key = Keys.hmacShaKeyFor("Bi4WBs4WIVla21ZqA82AcrQE8XzSClctXt1klxIB4eX1YPznKgF5XUK99jJDefo9pXFfNo7kufeVxvQkmdSpaQ==".getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String username, String userType,Integer canteenId) {
        Instant now = Instant.now();

        Instant expiry = userType.equals("user")
                ? now.plus(1, ChronoUnit.DAYS)       // staff: 1 day
                : now.plus(6, ChronoUnit.DAYS);     // canteen: 6 hours

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .addClaims(Map.of("userType", userType))
                .addClaims(Map.of("canteenId",canteenId))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getUserType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userType", String.class);
    }

    public static boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
