package com.jasmine.dev.OrderService.command.api.saga;

import com.jasmine.dev.CommonService.commands.*;
import com.jasmine.dev.CommonService.dto.UserDTO;
import com.jasmine.dev.CommonService.events.*;
import com.jasmine.dev.CommonService.queries.GetUserPaymentDetailsQuery;
import com.jasmine.dev.OrderService.command.api.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient CommandGateway commandGateway;

    public OrderProcessingSaga(){

    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent orderCreatedEvent){
        log.info("order created event saga for orderId: {}", orderCreatedEvent.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(orderCreatedEvent.getUserId());

        UserDTO user = null;

        try{
            user =queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(UserDTO.class)).join();
        }catch (Exception e){
            log.error(e.getMessage());
            cancelOrderCommand(orderCreatedEvent.getOrderId());
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDTO(user.getCardDTO())
                .orderId(orderCreatedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent paymentProcessedEvent){
        log.info("payment processed event saga for orderId: {}", paymentProcessedEvent.getOrderId());

        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);
        }catch (Exception e){
            log.error(e.getMessage());
            cancelPaymentCommand(paymentProcessedEvent);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent paymentProcessedEvent) {
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(paymentProcessedEvent.getPaymentId(), paymentProcessedEvent.getOrderId());
        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderShippedEvent orderShippedEvent){
        log.info("Order shipped event saga for orderId: {}", orderShippedEvent.getOrderId());

        try {
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                    .orderId(orderShippedEvent.getOrderId())
                    .orderStatus("APPROVED")
                    .build();

            commandGateway.send(completeOrderCommand);
        }catch (Exception e){
            log.error(e.getMessage());
            cancelShipmentCommand(orderShippedEvent);
        }
    }

    private void cancelShipmentCommand(OrderShippedEvent orderShippedEvent) {
        CancelShipmentCommand cancelShipmentCommand = new CancelShipmentCommand(orderShippedEvent.getShipmentId(), orderShippedEvent.getOrderId());
        commandGateway.send(cancelShipmentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    private void handle(OrderCompletedEvent orderCompletedEvent){
        log.info("Order completed event saga for orderId: {}", orderCompletedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCancelledEvent orderCancelledEvent){
        log.info("Order cancelled event saga for orderId: {}", orderCancelledEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent paymentCancelledEvent){
        log.info("Payment cancelled event saga for orderId: {}", paymentCancelledEvent.getOrderId());
        cancelOrderCommand(paymentCancelledEvent.getOrderId());
    }


}
