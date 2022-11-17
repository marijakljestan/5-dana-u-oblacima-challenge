package com.hakaton.challenge.services;

import com.hakaton.challenge.constants.OrderConstants;
import com.hakaton.challenge.domain.*;
import com.hakaton.challenge.repository.OrderRepository;
import com.hakaton.challenge.repository.TradeRepository;
import com.hakaton.challenge.service.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.hakaton.challenge.constants.OrderConstants.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTests {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private TradeRepository tradeRepositoryMock;

    @Mock
    private OrderEntity orderEntityMock;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessBuyOrder() {
        List<OrderEntity> orders = getSellOrdersForProcessing();
        OrderEntity newOrderEntity = OrderEntity.builder().type(OrderType.BUY).price(9.0).quantity(50.0).currencyPair(DB_CURRENCY_PAIR).orderStatus(OrderStatus.OPEN).filledQuantity(0.0).trades(new ArrayList<>()).build();
        TradeEntity tradeEntity = TradeEntity.builder().buyOrderId(newOrderEntity.getId()).build();

        when(orderRepositoryMock.findSuitableSellOrders(newOrderEntity.getPrice())).thenReturn(orders);
        when(orderRepositoryMock.save(newOrderEntity)).thenReturn(newOrderEntity);
        when(tradeRepositoryMock.fetchTradesByOrder(newOrderEntity.getId())).thenReturn(Arrays.asList(tradeEntity));

        OrderEntity processedOrder = orderService.processOrder(newOrderEntity);

        Assert.assertEquals(processedOrder.getOrderStatus(), OrderStatus.CLOSED);
        Assert.assertEquals(Optional.ofNullable(processedOrder.getFilledQuantity()), Optional.of(50.0));
        verify(orderRepositoryMock, times(1)).findSuitableSellOrders(newOrderEntity.getPrice());
    }

    @Test
    public void testProcessSellOrder() {
        List<OrderEntity> orders = getBuyOrdersForProcessing();
        OrderEntity newOrderEntity = OrderEntity.builder().type(OrderType.SELL).price(7.0).quantity(100.0).currencyPair(DB_CURRENCY_PAIR).orderStatus(OrderStatus.OPEN).filledQuantity(0.0).trades(new ArrayList<>()).build();
        TradeEntity tradeEntity = TradeEntity.builder().sellOrderId(newOrderEntity.getId()).build();

        when(orderRepositoryMock.findSuitableBuyOrders(newOrderEntity.getPrice())).thenReturn(orders);
        when(orderRepositoryMock.save(newOrderEntity)).thenReturn(newOrderEntity);
        when(tradeRepositoryMock.fetchTradesByOrder(newOrderEntity.getId())).thenReturn(Arrays.asList(tradeEntity));

        OrderEntity processedOrder = orderService.processOrder(newOrderEntity);

        Assert.assertEquals(processedOrder.getOrderStatus(), OrderStatus.CLOSED);
        Assert.assertEquals(Optional.ofNullable(processedOrder.getFilledQuantity()), Optional.of(100.0));
    }

    @Test
    public void testLoadOrderbook() {
        List<OrderEntity> orders = getActiveOrdersForOrderbook();
        when(orderRepositoryMock.findActiveOrders()).thenReturn(orders);

        OrderbookEntity orderbook = orderService.LoadOrderBook();

        Assert.assertEquals(orderbook.getBuyOrders().size(), 2);
        Assert.assertEquals(orderbook.getSellOrders().size(), 1);
        verify(orderRepositoryMock, times(1)).findActiveOrders();
        verifyNoMoreInteractions(orderRepositoryMock);
    }

    @Test
    public void testFindOrderById() {
        when(orderRepositoryMock.findById(OrderConstants.DB_ORDER_ID)).thenReturn(Optional.of(orderEntityMock));

        OrderEntity orderDb = orderService.findOrderById(OrderConstants.DB_ORDER_ID);

        Assert.assertEquals(orderEntityMock, orderDb);
        verify(orderRepositoryMock, times(1)).findById(OrderConstants.DB_ORDER_ID);
        verifyNoMoreInteractions(orderRepositoryMock);
    }

    private List<OrderEntity> getBuyOrdersForProcessing() {
        OrderEntity order1 = OrderEntity.builder().id(1).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(250.0).price(7.0).trades(new ArrayList<>()).build();
        OrderEntity order2 = OrderEntity.builder().id(2).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(800.0).price(6.0).trades(new ArrayList<>()).build();
        OrderEntity order3 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(150.0).price(5.0).trades(new ArrayList<>()).build();
        OrderEntity order4 = OrderEntity.builder().id(4).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(5000.0).price(1.0).trades(new ArrayList<>()).build();

        List<OrderEntity> buyOrders = new ArrayList<>();
        buyOrders.add(order1);
        buyOrders.add(order2);
        buyOrders.add(order3);
        buyOrders.add(order4);

        return buyOrders;
    }

    private List<OrderEntity> getSellOrdersForProcessing() {
        OrderEntity order1 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(100.0).price(9.0).trades(new ArrayList<>()).build();
        OrderEntity order2 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(50.0).price(10.0).trades(new ArrayList<>()).build();
        OrderEntity order3 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(1000.0).price(11.0).trades(new ArrayList<>()).build();
        OrderEntity order4 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(250.0).price(15.0).trades(new ArrayList<>()).build();

        List<OrderEntity> sellOrders = new ArrayList<>();
        sellOrders.add(order1);
        sellOrders.add(order2);
        sellOrders.add(order3);
        sellOrders.add(order4);

        return sellOrders;
    }

    private List<OrderEntity> getActiveOrdersForOrderbook() {
        OrderEntity order1 = OrderEntity.builder().id(1).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(250.0).price(10.0).build();
        OrderEntity order2 = OrderEntity.builder().id(2).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(50.0).price(10.0).build();
        OrderEntity order3 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.BUY)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(150.0).price(7.0).build();

        OrderEntity order4 = OrderEntity.builder().id(4).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(150.0).price(7.0).build();
        OrderEntity order5 = OrderEntity.builder().id(3).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS).type(OrderType.SELL)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(100.0).price(7.0).build();

        List<OrderEntity> allOrders = new ArrayList<>();
        allOrders.add(order1);
        allOrders.add(order2);
        allOrders.add(order3);
        allOrders.add(order4);
        allOrders.add(order5);

        return allOrders;
    }
}
