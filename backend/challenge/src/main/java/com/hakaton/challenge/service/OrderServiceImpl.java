package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.api.Trade;
import com.hakaton.challenge.domain.*;
import com.hakaton.challenge.dto.OrderDto;
import com.hakaton.challenge.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order processOrder(Order order) {
        //VALIDATE ORDER PRECISION, VALUTE ETC
        //REFACTOR WITH DEFAULT VALUES
        processOrderByType(order);

        /*order.setFilledQuantity(0.0);
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCreatedDateTime(new Date());
        order.setTrades(new ArrayList<>());
        OrderEntity orderEntity = saveOrder(order);*/
        return order;
    }

    private void processOrderByType(Order order) {
        if(order.getType().equals(OrderType.BUY))
            processBuyOrder(order);
        else
            processSellOrder(order);
    }

    private void processBuyOrder(Order order) {
        OrderbookEntity orderBook = LoadOrderBook();
        List<OrderDto> suitableOrders = orderBook.FindSuitableSellOrders(order.getPrice());
        if(suitableOrders.isEmpty()) {
            CreateNewOrder(order);
            return;
        }

        double difference = order.getQuantity();
        for(OrderDto dto: suitableOrders){
            difference =  order.getQuantity() - dto.getQuantity();
            if (difference < 0){
                orderBook.DecreaseAmountOfSellOrder(order.getPrice(), Math.abs(difference));
                return;
            }

            orderBook.RemoveSellOrderFromOrderbookWithPrice(dto.getPrice());
            if(difference == 0)
                return;
        }

        if(difference > 0) {
            order.setQuantity(difference);
            CreateNewOrder(order);
        }
    }


    private void processSellOrder(Order order) {
        OrderEntity newOrder = saveOrder(order);
        List<OrderEntity> suitableBuyOrders = orderRepository.findSuitableBuyOrders(order.getPrice());
        suitableBuyOrders.sort(Comparator.comparingDouble(OrderEntity::getPrice).reversed());
        double leftQuantity = order.getQuantity();
        for(OrderEntity o : suitableBuyOrders){
            double oCapacity = o.getQuantity() - o.getFilledQuantity();
            double difference = oCapacity - leftQuantity;
            if(difference > 0){
                o.setFilledQuantity(o.getFilledQuantity() + leftQuantity);
                orderRepository.save(o);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + leftQuantity);
                TradeEntity trade = TradeEntity.builder()
                                                .buyOrderId(o.getId()).sellOrderId(newOrder.getId())
                                                .createdDateTime(new Date()).price(o.getPrice()).quantity(leftQuantity).build();
                newOrder.getTrades().add(trade);
                leftQuantity = 0;
            }
            else if(difference <= 0) {
                o.setFilledQuantity(o.getQuantity());
                o.setOrderStatus(OrderStatus.CLOSED);
                orderRepository.save(o);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + oCapacity);
                TradeEntity trade = TradeEntity.builder()
                                               .buyOrderId(o.getId()).sellOrderId(newOrder.getId())
                                               .createdDateTime(new Date()).price(o.getPrice()).quantity(oCapacity)
                                                .build();
                newOrder.getTrades().add(trade);
                leftQuantity -= oCapacity;
            }
            if (leftQuantity == 0) {
                newOrder.setOrderStatus(OrderStatus.CLOSED);
                orderRepository.save(newOrder);
                return;
            }
        }
        if(leftQuantity > 0){
            newOrder.setQuantity(leftQuantity);
            newOrder.setFilledQuantity(0.0);
            orderRepository.save(newOrder);
        }
    }

    private void CreateNewOrder(Order order) {
        order.setFilledQuantity(0.0);
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCreatedDateTime(new Date());
        order.setTrades(new ArrayList<>());
        OrderEntity orderEntity = saveOrder(order);
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
        orderBook.getBuyOrders().sort(Comparator.comparingDouble(OrderDto::getPrice).reversed());
        orderBook.getSellOrders().sort(Comparator.comparingDouble(OrderDto::getPrice));
        return orderBook;
    }


    @Override
    public Order getOrderById(Integer id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    public void deleteAll() {
        //DELETE TRADES -> CASCADE!!
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
