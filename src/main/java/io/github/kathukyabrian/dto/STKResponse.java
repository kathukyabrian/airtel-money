package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class STKResponse {
    private String reference;
    private String responseCode;
    private String responseDescription;
    private boolean success;
}
