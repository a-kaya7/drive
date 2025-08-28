package net.drive.config;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.drive.model.entities.administration.allgemein.Benutzer;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long EXPIRATION_TIME;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
    	byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.EXPIRATION_TIME = expiration;
    }

    public String generateToken(Benutzer benutzer) {
        return Jwts.builder()
        		.setHeaderParam("typ", "JWT")
        		.setSubject(benutzer.getBenutzerkennung())
                .claim("benutzergruppe", benutzer.getBenutzergruppe().getBenutzergruppe())
                .claim("benutzerkennung", benutzer.getBenutzerkennung())
                .claim("mandant", benutzer.getMandant().getIdname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String getBenutzerkennungFromToken(String token) {
        return parseToken(token).get("benutzerkennung", String.class);
    }
    
    public String getBenutzergruppeFromToken(String token) {
        return parseToken(token).get("benutzergruppe", String.class);
    }
    public String getMandantFromToken(String token) {
        return parseToken(token).get("mandant", String.class);
    }
}
