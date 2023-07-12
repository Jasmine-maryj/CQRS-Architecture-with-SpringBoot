package com.jasmine.dev.ShipmentService.command.api.aggregate;

import com.jasmine.dev.CommonService.commands.CancelPaymentCommand;
import com.jasmine.dev.CommonService.commands.CancelShipmentCommand;
import com.jasmine.dev.CommonService.commands.ShipOrderCommand;
import com.jasmine.dev.CommonService.events.OrderShippedEvent;
import com.jasmine.dev.CommonService.events.PaymentCancelledEvent;
import com.jasmine.dev.CommonService.events.ShipmentCancelledEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@AllArgsConstructor
public class ShipmentAggregate {

    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;

    public ShipmentAggregate(){

    }

    @CommandHandler
    public  ShipmentAggregate(ShipOrderCommand shipOrderCommand){
        OrderShippedEvent orderShippedEvent = OrderShippedEvent.builder()
                .shipmentId(shipOrderCommand.getShipmentId())
                .orderId(shipOrderCommand.getOrderId())
                .shipmentStatus("COMPLETED")
                .build();

        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent){
        this.orderId = orderShippedEvent.getOrderId();
        this.shipmentId = orderShippedEvent.getShipmentId();
        this.shipmentStatus = orderShippedEvent.getShipmentStatus();
    }

    @CommandHandler
    public void handle(CancelShipmentCommand cancelShipmentCommand){
        ShipmentCancelledEvent shipmentCancelledEvent = new ShipmentCancelledEvent();
        BeanUtils.copyProperties(cancelShipmentCommand, shipmentCancelledEvent);
        AggregateLifecycle.apply(shipmentCancelledEvent);
    }

    public void on(ShipmentCancelledEvent shipmentCancelledEvent){
        this.shipmentStatus = shipmentCancelledEvent.getShipmentStatus();
    }
}
