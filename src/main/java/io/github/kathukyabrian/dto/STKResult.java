package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class STKResult {
    private String referenceId;
    private String code;
    private String description;
    private String transactionCode;
    private boolean success;
}
