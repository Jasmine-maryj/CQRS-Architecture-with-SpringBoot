package com.dev.jasmine.cqrsarchitecture.query.api.projection;

import com.dev.jasmine.cqrsarchitecture.command.api.data.Product;
import com.dev.jasmine.cqrsarchitecture.command.api.dto.ProductRestDTO;
import com.dev.jasmine.cqrsarchitecture.command.api.repository.ProductRepository;
import com.dev.jasmine.cqrsarchitecture.query.api.queries.GetProductsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductProjection {

    private ProductRepository productRepository;

    public ProductProjection(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestDTO> handle(GetProductsQuery getProductsQuery){
        List<Product> productList = productRepository.findAll();
        List<ProductRestDTO> productRestDTOList = productList.stream()
                .map(product -> ProductRestDTO.builder()
                        .quantity(product.getQuantity())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build())
                .collect(Collectors.toList());
        return productRestDTOList;
    }
}
