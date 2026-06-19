package io.github.kathukyabrian.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.AuthRequest;
import io.github.kathukyabrian.dto.AuthResponse;
import io.github.kathukyabrian.util.HttpUtil;
import okhttp3.MediaType;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Auth {
    private static LocalDateTime nextRefreshTime;
    private static String accessToken;

    public static String getAccessToken(Logger logger) {
        if (nextRefreshTime == null) {
            return getAuth(logger);
        }

        if (LocalDateTime.now().isBefore(nextRefreshTime)) {
            return accessToken;
        } else {
            return getAuth(logger);
        }
    }

    private static String getAuth(Logger logger) {
        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();
        String url = applicationProperties.getAuthUrl();

        AuthRequest authRequest = new AuthRequest(applicationProperties.getClientId(), applicationProperties.getClientSecret());

        String response;
        try {
            response = HttpUtil.post(url, new ObjectMapper().writeValueAsString(authRequest), new HashMap<>(), MediaType.get("application/json; charset=utf-8"));
            AuthResponse authResponse = new ObjectMapper().readValue(response, AuthResponse.class);
            if (authResponse != null) {
                int expiresIn = Integer.parseInt(authResponse.getExpiresIn());
                accessToken = authResponse.getAccessToken();
                nextRefreshTime = LocalDateTime.now().plusMinutes(expiresIn);
            }
            return accessToken;
        } catch (IOException e) {
            logger.error("system|encountered an error while getting auth", e);
            return null;
        }
    }
}
