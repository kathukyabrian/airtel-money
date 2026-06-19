package io.github.kathukyabrian.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.*;
import io.github.kathukyabrian.util.HttpUtil;
import okhttp3.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Airtel {
    private static final Logger logger = LogManager.getLogger(Airtel.class);

    public static STKResponse requestPayment(Integer amount, String phoneNumber, String description) {
        ObjectMapper objectMapper = new ObjectMapper();

        String accessToken = Auth.getAccessToken(logger);

        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        AirtelSTKSubscriber subscriber = new AirtelSTKSubscriber(
                applicationProperties.getCountry(),
                applicationProperties.getCurrency(),
                phoneNumber
        );

        AirtelSTKTransaction transaction = new AirtelSTKTransaction(
                amount,
                applicationProperties.getCountry(),
                applicationProperties.getCurrency(),
                UUID.randomUUID().toString()
        );

        AirtelSTKRequest airtelSTKRequest = new AirtelSTKRequest(description, subscriber, transaction);

        String url = applicationProperties.getPaymentUrl();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(airtelSTKRequest);
        } catch (com.fasterxml.jackson.core.JsonProcessingException ignore) {
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);
        headerMap.put("X-Country", applicationProperties.getCountry());
        headerMap.put("X-Currency", applicationProperties.getCurrency());

        String response = null;
        try {
            response = HttpUtil.post(url, body, headerMap, MediaType.get("application/json; charset=utf-8"));
        } catch (IOException e) {
            logger.error("Encountered exception ", e);
        }

        logger.info("system|got response from daraja: {}", response);
        AirtelSTKResponse stkResponse;
        try {
            stkResponse = objectMapper.readValue(response, AirtelSTKResponse.class);
        } catch (JsonProcessingException ex) {
            return null;
        }

        STKResponse finalResponse = new STKResponse();
        finalResponse.setReferenceId(transaction.getId());
        finalResponse.setResponseCode(stkResponse.getStatus().getResponseCode());
        finalResponse.setResponseDescription(stkResponse.getStatus().getMessage());
        finalResponse.setSuccess(stkResponse.getStatus().isSuccess());

        return finalResponse;

    }

    public static STKResult handleResult(AirtelSTKResult result) {
        STKResult response = new STKResult();

        response.setReferenceId(result.getId());
        response.setCode(result.getStatusCode());
        response.setDescription(result.getMessage());
        response.setSuccess("TS".equals(result.getStatusCode()));
        response.setTransactionCode(result.getAirtelMoneyId());

        return response;
    }
}
