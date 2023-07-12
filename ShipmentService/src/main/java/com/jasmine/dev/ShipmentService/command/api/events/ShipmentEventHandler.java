package com.jasmine.dev.ShipmentService.command.api.events;

import com.jasmine.dev.CommonService.events.OrderShippedEvent;
import com.jasmine.dev.CommonService.events.PaymentCancelledEvent;
import com.jasmine.dev.CommonService.events.ShipmentCancelledEvent;
import com.jasmine.dev.ShipmentService.command.api.dto.Shipment;
import com.jasmine.dev.ShipmentService.command.api.dto.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    private ShipmentRepository shipmentRepository;

    public ShipmentEventHandler(ShipmentRepository shipmentRepository){
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent orderShippedEvent){
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(orderShippedEvent, shipment);
        shipmentRepository.save(shipment);
    }

    @EventHandler
    public void on(ShipmentCancelledEvent shipmentCancelledEvent){
        Shipment shipment = shipmentRepository.findById(shipmentCancelledEvent.getShipmentId()).get();
        shipment.setShipmentStatus(shipmentCancelledEvent.getShipmentStatus());
        shipmentRepository.save(shipment);
    }
}
