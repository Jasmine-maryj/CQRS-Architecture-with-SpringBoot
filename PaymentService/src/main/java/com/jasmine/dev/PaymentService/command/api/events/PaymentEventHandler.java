package com.jasmine.dev.PaymentService.command.api.events;

import com.jasmine.dev.CommonService.events.PaymentCancelledEvent;
import com.jasmine.dev.CommonService.events.PaymentProcessedEvent;
import com.jasmine.dev.PaymentService.command.api.dto.PaymentDTO;
import com.jasmine.dev.PaymentService.command.api.dto.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent){
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date())
                .build();

        paymentRepository.save(paymentDTO);
    }

    @EventHandler
    public void on(PaymentCancelledEvent paymentCancelledEvent){
        PaymentDTO paymentDTO = paymentRepository.findById(paymentCancelledEvent.getPaymentId()).get();
        paymentDTO.setPaymentStatus(paymentCancelledEvent.getPaymentStatus());
        paymentRepository.save(paymentDTO);
    }
}
