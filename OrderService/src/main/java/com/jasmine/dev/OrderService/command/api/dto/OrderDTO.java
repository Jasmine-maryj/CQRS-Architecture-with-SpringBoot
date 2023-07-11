package com.jasmine.dev.OrderService.command.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
//    private String orderStatus;
}
