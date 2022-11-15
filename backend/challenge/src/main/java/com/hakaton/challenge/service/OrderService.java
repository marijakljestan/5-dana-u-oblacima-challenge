package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderbookEntity;

public interface OrderService {
    Order processOrder(Order order);

    public OrderbookEntity LoadOrderBook();

    Order getOrderById(Integer id);

    void deleteAll();
}
