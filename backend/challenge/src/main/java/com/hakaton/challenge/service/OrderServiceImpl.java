package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.*;
import com.hakaton.challenge.domain.OrderbookItem;
import com.hakaton.challenge.dto.CreateTradeDto;
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

        double remainOrderQuantity = order.getQuantity();
        for(OrderEntity suitableOrder : suitableOrders){
            double availableQuantityOfSuitableOrder = suitableOrder.getQuantity() - suitableOrder.getFilledQuantity();
            if(availableQuantityOfSuitableOrder > remainOrderQuantity){
                suitableOrder.setFilledQuantity(suitableOrder.getFilledQuantity() + remainOrderQuantity);
                CreateTradeDto newTrade = CreateTradeDto.builder().newOrder(newOrder).existingOrder(suitableOrder).quantity(remainOrderQuantity).build();
                createTrade(newTrade);
                remainOrderQuantity = 0;
            } else {
                suitableOrder.setFilledQuantity(suitableOrder.getQuantity());
                CreateTradeDto newTrade = CreateTradeDto.builder().newOrder(newOrder).existingOrder(suitableOrder).quantity(availableQuantityOfSuitableOrder).build();
                createTrade(newTrade);
                closeOrder(suitableOrder);
                remainOrderQuantity -= availableQuantityOfSuitableOrder;
            }
            if (remainOrderQuantity == 0)
                return closeOrder(newOrder);

        }
        if(remainOrderQuantity > 0){
            if(newOrder.getFilledQuantity() == 0) return newOrder;

            closeOrder(newOrder);
            order.setId(0);
            order.setQuantity(remainOrderQuantity);
            saveOrder(order);
        }
        return newOrder;
    }

    private void createTrade (CreateTradeDto tradeDto) {
        TradeEntity trade = tradeDto.getNewOrder().getType().equals(OrderType.BUY)?
                createTradeWithSellOrder(tradeDto.getNewOrder(), tradeDto.getExistingOrder()) : createTradeWithBuyOrder(tradeDto.getExistingOrder(), tradeDto.getNewOrder());
        trade.setQuantity(tradeDto.getQuantity());
        trade.setPrice(tradeDto.getExistingOrder().getPrice());
        tradeDto.getNewOrder().setFilledQuantity(tradeDto.getNewOrder().getFilledQuantity() + trade.getQuantity());
        tradeDto.getNewOrder().getTrades().add(trade);
        orderRepository.save(tradeDto.getNewOrder());
        tradeDto.getExistingOrder().getTrades().add(trade);
        orderRepository.save(tradeDto.getExistingOrder());
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

    public OrderbookEntity LoadOrderBook(){
        OrderbookEntity orderBook = new OrderbookEntity();
        List<OrderEntity> orders = orderRepository.findActiveOrders();
        for(OrderEntity order : orders){
            if(order.getType().equals(OrderType.BUY))
                orderBook.LoadBuyOrder(order);
            else
                orderBook.LoadSellOrder(order);
        }

        orderBook.getBuyOrders().sort(Comparator.comparingDouble(OrderbookItem::getPrice).reversed());
        orderBook.getSellOrders().sort(Comparator.comparingDouble(OrderbookItem::getPrice));
        return orderBook;
    }


    @Override
    public Order FindOrderById(Integer id) {
        OrderEntity orderEntity = orderRepository.fetchWithTrades(id);
        return modelMapper.map(orderEntity, Order.class);
    }

    public OrderEntity findOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
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
