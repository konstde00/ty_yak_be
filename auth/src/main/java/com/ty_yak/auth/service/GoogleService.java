package com.ty_yak.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ty_yak.auth.exception.JwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleService {

    @SneakyThrows
    public String parseToken(String token) {

        log.info("'parseToken' invoked");

        var verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory()).build();

        try {

            var idToken = verifier.verify(token);

            var payload = idToken.getPayload();

            var email = payload.getEmail();

            log.info("'parseToken' returned - {}", email);

            return email;

        } catch (Exception e) {

            log.error(e.getLocalizedMessage(), e);
            throw new JwtException("Google token not valid.");
        }
    }
}
