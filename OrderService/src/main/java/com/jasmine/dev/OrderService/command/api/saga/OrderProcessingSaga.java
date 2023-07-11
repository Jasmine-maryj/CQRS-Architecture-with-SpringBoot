package com.jasmine.dev.OrderService.command.api.saga;

import com.jasmine.dev.CommonService.commands.ShipOrderCommand;
import com.jasmine.dev.CommonService.commands.ValidatePaymentCommand;
import com.jasmine.dev.CommonService.dto.UserDTO;
import com.jasmine.dev.CommonService.events.PaymentProcessedEvent;
import com.jasmine.dev.CommonService.queries.GetUserPaymentDetailsQuery;
import com.jasmine.dev.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
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
    private QueryGateway queryGateway;

    @Autowired
    private CommandGateway commandGateway;

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
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDTO(user.getCardDTO())
                .orderId(orderCreatedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
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
        }
    }


}
