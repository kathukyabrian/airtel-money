package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirtelSTKResponse {
    private AirtelSTKResponseData reference;
    private AirtelSTKStatus status;
}
