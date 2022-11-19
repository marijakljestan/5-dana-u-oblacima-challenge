package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderbookEntity;
import org.springframework.web.server.ResponseStatusException;

public interface OrderService {
    Order ProcessOrder(Order order) throws ResponseStatusException;

    OrderbookEntity LoadOrderBook(String pair);

    Order FetchOrderByIdWithTrades(Integer id);

    void deleteAll();
}
