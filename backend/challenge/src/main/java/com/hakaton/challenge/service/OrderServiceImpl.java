package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    @Override
    public Order processOrder(Order order) {
        return null;
    }

    @Override
    public Order getOrderById(Integer id) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
