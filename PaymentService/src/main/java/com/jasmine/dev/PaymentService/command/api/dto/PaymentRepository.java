package com.jasmine.dev.PaymentService.command.api.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentDTO, String> {
}
