package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirtelSTKTransaction {
    private Integer amount;
    private String country;
    private String currency;
    private String id;
}
