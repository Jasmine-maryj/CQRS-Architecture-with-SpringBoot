package com.dev.jasmine.cqrsarchitecture.command.api.aggregate;

import com.dev.jasmine.cqrsarchitecture.command.api.commands.CreateProductCommand;
import com.dev.jasmine.cqrsarchitecture.command.api.dto.ProductRestModel;
import com.dev.jasmine.cqrsarchitecture.command.api.events.ProductCreatedEvent;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregator {

    @AggregateIdentifier
    private String productId;

    private String name;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregator(CreateProductCommand createProductCommand){

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    public ProductAggregator(){
        
    }
}
