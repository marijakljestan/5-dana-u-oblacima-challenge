package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.*;
import com.hakaton.challenge.domain.OrderbookItem;
import com.hakaton.challenge.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order processOrder(Order order) {
        order.setId(0);
        OrderEntity createdOrder = processOrderByType(order);
        return modelMapper.map(createdOrder, Order.class);
    }

    private OrderEntity processOrderByType(Order order) {
        if(order.getType().equals(OrderType.BUY))
            return processBuyOrder(order);
        else
            return processSellOrder(order);
    }

    private OrderEntity processBuyOrder(Order order) {
        OrderEntity newOrder = saveOrder(order);
        List<OrderEntity> suitableSellOrders = orderRepository.findSuitableSellOrders(order.getPrice());
        double leftQuantity = order.getQuantity();
        for(OrderEntity o : suitableSellOrders){
            double oCapacity = o.getQuantity() - o.getFilledQuantity();
            double difference = oCapacity - leftQuantity;
            if(difference > 0){
                o.setFilledQuantity(o.getFilledQuantity() + leftQuantity);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + leftQuantity);
                TradeEntity trade = TradeEntity.builder()
                        .sellOrderId(o.getId()).buyOrderId(newOrder.getId())
                        .createdDateTime(new Date()).price(o.getPrice()).quantity(leftQuantity).build();
                newOrder.getTrades().add(trade);
                o.getTrades().add(trade);
                orderRepository.save(o);
                leftQuantity = 0;
            }
            else if(difference <= 0) {
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + oCapacity);
                TradeEntity trade = TradeEntity.builder()
                        .sellOrderId(o.getId()).buyOrderId(newOrder.getId())
                        .createdDateTime(new Date()).price(o.getPrice()).quantity(oCapacity)
                        .build();
                newOrder.getTrades().add(trade);
                o.setFilledQuantity(o.getQuantity());
                o.setOrderStatus(OrderStatus.CLOSED);
                o.getTrades().add(trade);
                orderRepository.save(o);
                leftQuantity -= oCapacity;
            }
            if (leftQuantity == 0) {
                newOrder.setOrderStatus(OrderStatus.CLOSED);
                orderRepository.save(newOrder);
                break;
            }
        }
        if(leftQuantity > 0){
            newOrder.setQuantity(leftQuantity);
            newOrder.setFilledQuantity(0.0);
            orderRepository.save(newOrder);
        }
        return newOrder;
    }


    private OrderEntity processSellOrder(Order order) {
        OrderEntity newOrder = saveOrder(order);
        List<OrderEntity> suitableBuyOrders = orderRepository.findSuitableBuyOrders(order.getPrice());
        double leftQuantity = order.getQuantity();
        for(OrderEntity o : suitableBuyOrders){
            double oCapacity = o.getQuantity() - o.getFilledQuantity();
            double difference = oCapacity - leftQuantity;
            if(difference > 0){
                o.setFilledQuantity(o.getFilledQuantity() + leftQuantity);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + leftQuantity);
                TradeEntity trade = TradeEntity.builder()
                                                .buyOrderId(o.getId()).sellOrderId(newOrder.getId())
                                                .createdDateTime(new Date()).price(o.getPrice()).quantity(leftQuantity).build();
                newOrder.getTrades().add(trade);
                o.getTrades().add(trade);
                orderRepository.save(o);
                leftQuantity = 0;
            }
            else if(difference <= 0) {
                o.setFilledQuantity(o.getQuantity());
                o.setOrderStatus(OrderStatus.CLOSED);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + oCapacity);
                TradeEntity trade = TradeEntity.builder()
                                               .buyOrderId(o.getId()).sellOrderId(newOrder.getId())
                                               .createdDateTime(new Date()).price(o.getPrice()).quantity(oCapacity)
                                                .build();
                newOrder.getTrades().add(trade);
                o.getTrades().add(trade);
                orderRepository.save(o);
                leftQuantity -= oCapacity;
            }
            if (leftQuantity == 0) {
                newOrder.setOrderStatus(OrderStatus.CLOSED);
                orderRepository.save(newOrder);
                break;
            }
        }
        if(leftQuantity > 0){
            order.setQuantity(leftQuantity);
            saveOrder(order);
        }
        return newOrder;
    }

    public OrderbookEntity LoadOrderBook(){
        OrderbookEntity orderBook = new OrderbookEntity();
        List<OrderEntity> orders = orderRepository.findActiveOrders();
        for(OrderEntity order : orders){
            if(order.getType().equals(OrderType.BUY))
                orderBook.LoadBuyOrder(order);
            else
                orderBook.LoadSellOrder(order);
        }
        //orderBook.setBuyOrders(orderRepository.findActiveBuyOrders());
        //orderBook.setSellOrders(orderRepository.findActiveSellOrders());
        orderBook.getBuyOrders().sort(Comparator.comparingDouble(OrderbookItem::getPrice).reversed());
        orderBook.getSellOrders().sort(Comparator.comparingDouble(OrderbookItem::getPrice));
        return orderBook;
    }


    @Override
    public Order FindOrderById(Integer id) {
        OrderEntity orderEntity = orderRepository.fetchWithTrades(id);
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    private OrderEntity saveOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setOrderStatus(OrderStatus.OPEN);
        orderEntity.setCreatedDateTime(new Date());
        orderEntity.setTrades(new ArrayList<>());
        orderEntity.setFilledQuantity(0.0);
        return orderRepository.save(orderEntity);
    }
}
