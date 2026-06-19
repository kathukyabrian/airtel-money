package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirtelSTKRequest {
    private String reference;
    private AirtelSTKSubscriber subscriber;
    private AirtelSTKTransaction transaction;
}
