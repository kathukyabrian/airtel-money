package io.github.kathukyabrian.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AirtelSTKStatus {
    private String code;

    private String message;

    @JsonProperty("result_code")
    private String resultCode;

    @JsonProperty("response_code")
    private String responseCode;

    private boolean success;
}
