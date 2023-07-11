package com.dev.jasmine.cqrsarchitecture.query.api.controller;

import com.dev.jasmine.cqrsarchitecture.command.api.dto.ProductRestDTO;
import com.dev.jasmine.cqrsarchitecture.query.api.queries.GetProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private QueryGateway queryGateway;

    public ProductQueryController(QueryGateway queryGateway){
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ProductRestDTO>  getAllProducts(){
        GetProductsQuery getProductsQuery = new GetProductsQuery();

        List<ProductRestDTO> productRestDTOList = queryGateway.query(getProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestDTO.class)).join();
        return productRestDTOList;
    }
}
