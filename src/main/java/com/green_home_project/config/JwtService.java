package com.green_home_project.config;

import com.green_home_project.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // مفتاح سري لتوقيع التوكن (تقدر تولده بأي طريقة وتخليه ثابت)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // مدة صلاحية التوكن (مثال: ساعة واحدة)
    private final long expiration = 1000 * 60 * 60;

    // ===================== توليد توكن من USER كامل =====================
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())         // البريد الإلكتروني
                .claim("role", user.getRole().name()) // الدور
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // ===================== توليد توكن من ايميل فقط =====================
    // (في حالة تحتاجها، مش ضروري حاليا)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // ===================== استخراج الايميل من التوكن =====================
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ===================== التحقق من صلاحية التوكن =====================
    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}