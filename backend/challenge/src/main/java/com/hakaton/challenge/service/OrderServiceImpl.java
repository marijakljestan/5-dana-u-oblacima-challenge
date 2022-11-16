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
    public Order ProcessOrder(Order order) {
        order.setId(0);
        OrderEntity createdOrder = processOrder(order);
        return modelMapper.map(createdOrder, Order.class);
    }


    private OrderEntity processOrder(Order order) {
        OrderEntity newOrder = saveOrder(order);
        List<OrderEntity> suitableOrders = findSuitableOrders(order);

        double leftQuantity = order.getQuantity();
        for(OrderEntity suitableOrder : suitableOrders){
            double oCapacity = suitableOrder.getQuantity() - suitableOrder.getFilledQuantity();
            double difference = oCapacity - leftQuantity;
            if(difference > 0){
                suitableOrder.setFilledQuantity(suitableOrder.getFilledQuantity() + leftQuantity);
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + leftQuantity);
                TradeEntity trade = newOrder.getType().equals(OrderType.BUY)?
                        createTradeWithSellOrder(newOrder, suitableOrder) : createTradeWithBuyOrder(suitableOrder, newOrder);
                trade.setPrice(suitableOrder.getPrice());
                trade.setQuantity(leftQuantity);
                newOrder.getTrades().add(trade);
                suitableOrder.getTrades().add(trade);
                orderRepository.save(suitableOrder);
                leftQuantity = 0;
            }
            else if(difference <= 0) {
                newOrder.setFilledQuantity(newOrder.getFilledQuantity() + oCapacity);
                TradeEntity trade = newOrder.getType().equals(OrderType.BUY)?
                        createTradeWithSellOrder(newOrder, suitableOrder) : createTradeWithBuyOrder(suitableOrder, newOrder);
                trade.setQuantity(oCapacity);
                trade.setPrice(suitableOrder.getPrice());
                newOrder.getTrades().add(trade);
                orderRepository.save(newOrder);
                suitableOrder.setFilledQuantity(suitableOrder.getQuantity());
                suitableOrder.getTrades().add(trade);
                closeOrder(suitableOrder);
                leftQuantity -= oCapacity;
            }
            if (leftQuantity == 0)
                return closeOrder(newOrder);

        }
        if(leftQuantity > 0){
            if(newOrder.getFilledQuantity() == 0) return newOrder;

            closeOrder(newOrder);
            order.setId(0);
            order.setQuantity(leftQuantity);
            saveOrder(order);
        }
        return newOrder;
    }

    private List<OrderEntity> findSuitableOrders (Order order) {
        List<OrderEntity> suitableOrders = order.getType().equals(OrderType.BUY) ?
                                                orderRepository.findSuitableSellOrders(order.getPrice()) :
                                                orderRepository.findSuitableBuyOrders(order.getPrice());
        return suitableOrders;
    }

    private OrderEntity closeOrder(OrderEntity order) {
        order.setOrderStatus(OrderStatus.CLOSED);
        return orderRepository.save(order);
    }

    private TradeEntity createTradeWithBuyOrder (OrderEntity buyOrder, OrderEntity sellOrder) {
        return TradeEntity.builder()
                .buyOrderId(buyOrder.getId()).sellOrderId(sellOrder.getId()).createdDateTime(new Date())
                .build();
    }

    private TradeEntity createTradeWithSellOrder (OrderEntity buyOrder, OrderEntity sellOrder) {
        return TradeEntity.builder()
                .buyOrderId(buyOrder.getId()).sellOrderId(sellOrder.getId()).createdDateTime(new Date())
                .build();
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
