package com.jasmine.dev.OrderService.command.api.events;

import com.jasmine.dev.CommonService.events.OrderCancelledEvent;
import com.jasmine.dev.CommonService.events.OrderCompletedEvent;
import com.jasmine.dev.OrderService.data.Order;
import com.jasmine.dev.OrderService.data.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsHandler {

    private OrderRepository orderRepository;

    public OrderEventsHandler(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on (OrderCreatedEvent orderCreatedEvent){
        Order order = new Order();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent orderCompletedEvent){
        Order order = orderRepository.findById(orderCompletedEvent.getOrderId()).get();
        order.setOrderStatus(orderCompletedEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent orderCancelledEvent){
        Order order = orderRepository.findById(orderCancelledEvent.getOrderId()).get();
        order.setOrderStatus(orderCancelledEvent.getOrderStatus());
        orderRepository.save(order);
    }
}
