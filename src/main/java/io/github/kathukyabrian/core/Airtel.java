package io.github.kathukyabrian.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.AirtelSTKRequest;
import io.github.kathukyabrian.dto.AirtelSTKResponse;
import io.github.kathukyabrian.dto.AirtelSTKSubscriber;
import io.github.kathukyabrian.dto.AirtelSTKTransaction;
import io.github.kathukyabrian.dto.STKResponse;
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
        finalResponse.setReference(transaction.getId());
        finalResponse.setResponseCode(stkResponse.getStatus().getResponseCode());
        finalResponse.setResponseDescription(stkResponse.getStatus().getMessage());
        finalResponse.setSuccess(stkResponse.getStatus().isSuccess());

        return finalResponse;

    }

//    public static MpesaPaymentResult handleResult(MpesaResult mpesaResult) {
//        MpesaPaymentResult result = new MpesaPaymentResult();
//
//        DarajaSTKCallBack callBack = mpesaResult.getBody().getStkCallBack();
//        result.setMerchantRequestId(callBack.getMerchantRequestId());
//        result.setCode(callBack.getResultCode());
//        result.setDescription(callBack.getResultDescription());
//
//        if (mpesaResult.getBody().getStkCallBack().getCallBackMetaData() != null) {
//            for (CallBackItem callBackItem : mpesaResult.getBody().getStkCallBack().getCallBackMetaData().getItem()) {
//                if (callBackItem.getName().equals(ServiceConstants.MPESA_CALLBACK_REF_NO_KEY)) {
//                    result.setTransactionRef((String) callBackItem.getValue());
//                }
//
//                if (callBackItem.getName().equals(ServiceConstants.MPESA_CALLBACK_AMOUNT_KEY)) {
//                    result.setAmount(((Number) callBackItem.getValue()).intValue());
//                }
//
//                if (callBackItem.getName().equals(ServiceConstants.MPESA_CALLBACK_PHONE_NUMBER_KEY)) {
//                    result.setPhoneNumber(callBackItem.getValue() + "");
//                }
//            }
//        }
//        result.setSuccess(result.getCode().equals(0));
//
//        return result;
//    }

//    public static MpesaQueryTransactionResponse queryTransaction(String originatorConversationId) {
//        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        String accessToken = Auth.getAccessToken(logger);
//
//        QueryTransactionRequest request = new QueryTransactionRequest();
//        request.setInitiator(""); // todo: set appropriately
//        request.setSecurityCredential(""); // todo: set appropriately
//        request.setTransactionId(null);
//        request.setOriginatorConversationId(originatorConversationId);
//        request.setResultUrl(applicationProperties.getCallbackUrl()); //todo: check if there's need to change
//        try {
//            request.setPartyA(Long.valueOf(applicationProperties.getShortCode()));
//        } catch (NumberFormatException ignore) {
//        }
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + accessToken);
//
//        try {
//            String requestStr = objectMapper.writeValueAsString(request);
//            String response = HttpUtil.post(applicationProperties.getQueryUrl(), requestStr, headers, MediaType.get("application/json; charset=utf-8"));
//            QueryTransactionResponse queryTransactionResponse = objectMapper.readValue(response, QueryTransactionResponse.class);
//            return new MpesaQueryTransactionResponse(originatorConversationId, queryTransactionResponse.getResponseCode(), queryTransactionResponse.getResponseDescription(), "0".equals(queryTransactionResponse.getResponseCode()));
//        } catch (IOException ex) {
//            return new MpesaQueryTransactionResponse(originatorConversationId, "-1", ex.getMessage(), false);
//        }
//    }
}
