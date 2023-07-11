package com.jasmine.dev.CommonService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDTO {
    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}
