package com.jasmine.dev.CommonService.events;

import lombok.Data;

@Data
public class ShipmentCancelledEvent {
    private String shipmentId;
    private String orderId;
    private String shipmentStatus = "CANCELLED";
}
