package com.jasmine.dev.PaymentService.command.api.aggregate;

import com.jasmine.dev.CommonService.commands.ValidatePaymentCommand;
import com.jasmine.dev.CommonService.events.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate(){
    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        log.info("Executing validatePaymentCommand for orderId:{} and paymentId:{}", validatePaymentCommand.getOrderId(), validatePaymentCommand.getPaymentId());

        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId());
        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("PaymentProcessedEvent Applied!");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent){
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }

}