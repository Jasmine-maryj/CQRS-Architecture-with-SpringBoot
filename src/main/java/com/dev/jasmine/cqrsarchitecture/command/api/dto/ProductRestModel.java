package com.dev.jasmine.cqrsarchitecture.command.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRestModel {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
