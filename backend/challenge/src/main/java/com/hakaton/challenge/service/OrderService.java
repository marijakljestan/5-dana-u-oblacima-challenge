package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;

public interface OrderService {
    Order processOrder(Order order);

    Order getOrderById(Integer id);

    void deleteAll();
}
