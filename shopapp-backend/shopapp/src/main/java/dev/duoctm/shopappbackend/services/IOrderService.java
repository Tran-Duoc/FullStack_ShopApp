package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.dtos.OrderDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.models.Order;
import dev.duoctm.shopappbackend.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long id);

    List<Order> findByUserId(Long userId);
}
