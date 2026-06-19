package io.github.kathukyabrian.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AirtelSTKResult {
    private String id;

    private String message;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("airtel_money_id")
    private String airtelMoneyId;
}
