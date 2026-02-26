package com.example.app.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expMinutes:1440}")
    private long expMinutes;
    private Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(Long userId,String email){
        Date now= new Date();
        Date exp=new Date(now.getTime()+expMinutes*60_000);
        return Jwts.builder().setSubject(email).claim("uid",userId).setIssuedAt(now).setExpiration(exp).signWith(key(),SignatureAlgorithm.HS256).compact();
    }
    public String extractEmail(String token){
        return parseClaims(token).getBody().getSubject();
    }
    public Long extractUserId(String token){
        Object uid=parseClaims(token).getBody().get("uid");
        if (uid== null) return null;
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        return Long.parseLong(uid.toString());
    }
    public boolean isTokenValid(String token){
        try{
            parseClaims(token);
            return true;
        }catch (JwtException|IllegalArgumentException e){
            return false;
        }
    }
    private Jws<Claims> parseClaims(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
    }
}
