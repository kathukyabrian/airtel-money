package io.github.kathukyabrian.config;

import lombok.Data;

@Data
public class ApplicationProperties {
    private String clientId;
    private String clientSecret;
    private String authUrl;
    private String paymentUrl;
    private String queryUrl;
    private String country;
    private String currency;
}
