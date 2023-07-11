package com.jasmine.dev.CommonService.commands;

import com.jasmine.dev.CommonService.dto.CardDTO;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ValidatePaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private CardDTO cardDTO;
}
