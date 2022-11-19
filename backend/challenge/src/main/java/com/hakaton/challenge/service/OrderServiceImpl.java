package com.hakaton.challenge.service;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.*;
import com.hakaton.challenge.domain.OrderbookItem;
import com.hakaton.challenge.dto.CreateTradeDto;
import com.hakaton.challenge.exception.OrderNotFoundException;
import com.hakaton.challenge.repository.OrderRepository;
import com.hakaton.challenge.repository.TradeRepository;
import com.hakaton.challenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final TradeRepository tradeRepository;
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Override
    public Order ProcessOrder(Order order) throws ResponseStatusException {
        if(!doesUserExist(order.getUserId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User doesn't exists!");
        }
        OrderEntity newOrder = saveOrder(order);
        OrderEntity createdOrder = processOrder(newOrder);
        return modelMapper.map(createdOrder, Order.class);
    }

    private boolean doesUserExist(int userId){
        if(userRepository.findById(userId).orElse(null) == null){
            return false;
        }
        return true;
    }
    public OrderEntity processOrder(OrderEntity newOrder) {
        List<OrderEntity> suitableOrders = findSuitableOrders(newOrder);

        double remainOrderQuantity = newOrder.getQuantity();
        for(OrderEntity suitableOrder : suitableOrders){
            double availableQuantityOfSuitableOrder = suitableOrder.getQuantity() - suitableOrder.getFilledQuantity();
            double tradeQuantity = 0.0;
            if(availableQuantityOfSuitableOrder > remainOrderQuantity){
                suitableOrder.setFilledQuantity(suitableOrder.getFilledQuantity() + remainOrderQuantity);
                tradeQuantity = remainOrderQuantity;
            } else {
                suitableOrder.setFilledQuantity(suitableOrder.getQuantity());
                tradeQuantity = availableQuantityOfSuitableOrder;
                closeOrder(suitableOrder);
            }
            CreateTradeDto newTrade = CreateTradeDto.builder().newOrder(newOrder).existingOrder(suitableOrder).quantity(tradeQuantity).build();
            createTrade(newTrade);
            remainOrderQuantity -= tradeQuantity;
            if (remainOrderQuantity == 0) return closeOrder(newOrder);
        }

        if(remainOrderQuantity > 0)
            createNewOrderWithRemainingQuantity(newOrder, remainOrderQuantity);

        newOrder.setTrades(tradeRepository.fetchTradesByOrder(newOrder.getId()));
        return newOrder;
    }

    private void createNewOrderWithRemainingQuantity(OrderEntity newOrder, double remainOrderQuantity) {
        if(newOrder.getFilledQuantity() == 0) return;

        closeOrder(newOrder);
        Order orderWithRemainingQuantity = modelMapper.map(newOrder, Order.class);
        orderWithRemainingQuantity.setQuantity(remainOrderQuantity);
        saveOrder(modelMapper.map(orderWithRemainingQuantity, Order.class));
    }

    private void createTrade (CreateTradeDto tradeDto) {
        TradeEntity trade = tradeDto.getNewOrder().getType().equals(OrderType.BUY)?
                createTradeWithSellOrder(tradeDto.getNewOrder(), tradeDto.getExistingOrder()) : createTradeWithBuyOrder(tradeDto.getExistingOrder(), tradeDto.getNewOrder());
        trade.setQuantity(tradeDto.getQuantity());
        trade.setPrice(tradeDto.getExistingOrder().getPrice());
        tradeRepository.save(trade);
        tradeDto.getNewOrder().setFilledQuantity(tradeDto.getNewOrder().getFilledQuantity() + trade.getQuantity());
        orderRepository.save(tradeDto.getNewOrder());
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

    private List<OrderEntity> findSuitableOrders (OrderEntity order) {
        List<OrderEntity> suitableOrders = order.getType().equals(OrderType.BUY) ?
                                                orderRepository.findSuitableSellOrders(order.getPrice(),order.getCurrencyPair()) :
                                                orderRepository.findSuitableBuyOrders(order.getPrice(),order.getCurrencyPair());
        return suitableOrders;
    }

    private OrderEntity closeOrder(OrderEntity order) {
        order.setOrderStatus(OrderStatus.CLOSED);
        orderRepository.save(order);
        order.setTrades(tradeRepository.fetchTradesByOrder(order.getId()));
        return order;
    }

    public OrderbookEntity LoadOrderBook(String pair){
        OrderbookEntity orderBook = new OrderbookEntity();
        List<OrderEntity> orders = orderRepository.findActiveOrders(pair);
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
    public Order FetchOrderByIdWithTrades(Integer id) {
        OrderEntity orderEntity = orderRepository.fetchWithTrades(id);
        if(orderEntity == null) throw new OrderNotFoundException();
        orderEntity.setTrades(tradeRepository.fetchTradesByOrder(id));
        return modelMapper.map(orderEntity, Order.class);
    }

    public OrderEntity findOrderById(Integer id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if(order.isPresent())
            return order.get();

        throw new OrderNotFoundException();
    }

    @Override
    public void deleteAll() {  tradeRepository.deleteAll(); orderRepository.deleteAll(); userRepository.deleteAll();}

    private OrderEntity saveOrder(Order order) {
        order.setId(0);
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setOrderStatus(OrderStatus.OPEN);
        orderEntity.setCreatedDateTime(new Date());
        orderEntity.setTrades(new ArrayList<>());
        orderEntity.setFilledQuantity(0.0);
        orderEntity.setUser(userRepository.findById(order.getUserId()).get());
        return orderRepository.save(orderEntity);
    }
}
