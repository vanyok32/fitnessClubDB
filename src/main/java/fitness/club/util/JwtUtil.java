package fitness.club.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;


public class JwtUtil {
    private final  static SecretKey KEY;
    private static final long EXPIRATION;
    static {
        Properties props = new Properties();
        try (InputStream is = JwtUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Не найден application.properties", e);
        }

        String secret = props.getProperty("jwt.secret");
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT secret должен быть ≥32 символов");
        }

        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        EXPIRATION = Long.parseLong(props.getProperty("jwt.expiration", "604800000"));
    }
    public static String generate(Long id, String name, String type) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("name", name)
                .claim("type", type)  // CLIENT или COACH
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(KEY.getEncoded()))
                .compact();
    }

    public static Claims validate(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(KEY.getEncoded()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

