package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderbookEntity;

public interface OrderService {
    Order ProcessOrder(Order order);

    OrderbookEntity LoadOrderBook(String pair);

    Order FetchOrderByIdWithTrades(Integer id);

    void deleteAll();
}
