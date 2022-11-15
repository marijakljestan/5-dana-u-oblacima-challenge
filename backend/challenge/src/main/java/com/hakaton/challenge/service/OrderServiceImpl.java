package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderbookEntity;
import com.hakaton.challenge.domain.OrderEntity;
import com.hakaton.challenge.domain.OrderStatus;
import com.hakaton.challenge.domain.OrderType;
import com.hakaton.challenge.dto.OrderDto;
import com.hakaton.challenge.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order processOrder(Order order) {
        //REFACTOR WITH DEFAULT VALUES
        processOrderByType(order);

        order.setFilledQuantity(0.0);
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCreatedDateTime(new Date());
        order.setTrades(new ArrayList<>());
        OrderEntity orderEntity = saveOrder(order);
        return order;
    }

    private void processOrderByType(Order order) {
        if(order.getType().equals(OrderType.BUY))
            processBuyOrder(order);
        processSellOrder(order);
    }

    private void processBuyOrder(Order order) {
        OrderbookEntity orderBook = LoadOrderBook();
        for(OrderDto o: orderBook.getSellOrders()){

        }
    }

    private void processSellOrder(Order order) {
    }


    public OrderbookEntity LoadOrderBook(){
        OrderbookEntity orderBook = new OrderbookEntity();
        List<OrderEntity> orders = orderRepository.findAll();
        for(OrderEntity order : orders){
            if(order.getType().equals(OrderType.BUY))
                orderBook.LoadBuyOrder(order);
            else
                orderBook.LoadSellOrder(order);
        }
        return orderBook;
    }

    private void checkIfBuyOrderIsInOrderBook(OrderEntity order) {

    }

    @Override
    public Order getOrderById(Integer id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    public void deleteAll() {
        //DELETE TRADES
        orderRepository.deleteAll();
    }

    private OrderEntity saveOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        return orderRepository.save(orderEntity);
    }
}
