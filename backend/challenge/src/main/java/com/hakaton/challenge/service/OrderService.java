package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderbookEntity;

public interface OrderService {
    Order processOrder(Order order);

    OrderbookEntity LoadOrderBook();

    Order FindOrderById(Integer id);

    void deleteAll();
}
