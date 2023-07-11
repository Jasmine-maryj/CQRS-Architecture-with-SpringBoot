package com.dev.jasmine.cqrsarchitecture.command.api.repository;

import com.dev.jasmine.cqrsarchitecture.command.api.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
