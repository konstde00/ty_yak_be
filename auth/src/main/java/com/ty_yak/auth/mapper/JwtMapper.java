package com.ty_yak.auth.mapper;

import com.ty_yak.auth.model.dto.login.JwtDto;
import com.ty_yak.auth.model.entity.RefreshToken;
import com.ty_yak.auth.model.entity.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.ty_yak.auth.model.enums.TokenType.ACCESS;
import static com.ty_yak.auth.model.enums.TokenType.REFRESH;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;

@Component
public class JwtMapper {

    public String token(User user, Long expirationTime, SecretKey secretKey) {
        return Jwts.builder()
                .signWith(secretKey, HS512)
                .setIssuer(ACCESS.name())
                .setSubject(Long.toString(user.getId()))
                .setExpiration(new Date(expirationTime))
                .claim("role", user.getRoles())
                .compact();
    }

    public String refreshToken(User user, Long expirationTime, SecretKey secretKey, RefreshToken refresh) {
        return Jwts.builder()
                .signWith(secretKey, HS512)
                .setId(Long.toString(refresh.getId()))
                .setSubject(Long.toString(user.getId()))
                .setIssuer(REFRESH.name())
                .setExpiration(new Date(expirationTime))
                .compact();
    }

    public RefreshToken refreshTokenEntity(User user, Long expirationTime) {
        return RefreshToken
                .builder()
                .token("token")
                .createdAt(now())
                .expiredAt(ofEpochMilli(expirationTime).atZone(systemDefault()).toLocalDateTime())
                .user(user)
                .build();
    }

    public JwtDto toJwtDto(User user, Long tokenExpirationTime, String token, String refreshToken) {
        return JwtDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .roles(user.getRoles())
                .accessTokenCreationTime(now())
                .accessTokenExpirationTime(ofEpochMilli(tokenExpirationTime).atZone(systemDefault()).toLocalDateTime())
                .username(user.getUsername())
                .build();
    }
}
