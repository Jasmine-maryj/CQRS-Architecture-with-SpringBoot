package com.dev.jasmine.cqrsarchitecture;

import com.dev.jasmine.cqrsarchitecture.command.api.exception.ProductServiceEventsErrorHandler;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CqrsArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsArchitectureApplication.class, args);
	}

	@Autowired
	public void configure(EventProcessingConfigurer processingConfigurer){
		processingConfigurer.registerListenerInvocationErrorHandler("product", configuration -> new ProductServiceEventsErrorHandler());
	}
}
