package com.dev.jasmine.cqrsarchitecture.command.api.controller;

import com.dev.jasmine.cqrsarchitecture.command.api.commands.CreateProductCommand;
import com.dev.jasmine.cqrsarchitecture.command.api.dto.ProductRestDTO;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {


    private CommandGateway commandGateway;


    public ProductCommandController(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String addProduct(@RequestBody ProductRestDTO productRestDTO){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(productRestDTO.getName())
                .price(productRestDTO.getPrice())
                .quantity(productRestDTO.getQuantity())
                .build();

        String output = commandGateway.sendAndWait(createProductCommand);
        return output;
    }
}
