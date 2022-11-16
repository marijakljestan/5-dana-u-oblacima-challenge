package com.hakaton.challenge.model;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.hakaton.challenge.constants.OrderConstants.DB_CURRENCY_PAIR;

@RunWith(SpringRunner.class )
@SpringBootTest
public class OrderTest {

    @Test
    public void testIsBuyOrderValid(){
        Order order = Order.builder().type(OrderType.BUY).price(9.0).quantity(50.0).currencyPair(DB_CURRENCY_PAIR).build();
        Assert.assertTrue(order.IsValid());
    }

    @Test
    public void testIsSellOrderValid(){
        Order order = Order.builder().type(OrderType.SELL).price(7.0).quantity(30.0).currencyPair(DB_CURRENCY_PAIR).build();
        Assert.assertTrue(order.IsValid());
    }

    @Test
    public void testIsBuyOrderNotValid(){
        Order order = Order.builder().type(OrderType.BUY).price(9.0).quantity(-50.0).currencyPair(DB_CURRENCY_PAIR).build();
        Assert.assertFalse(order.IsValid());
    }

    @Test
    public void testIsSellOrderNotValid(){
        Order order = Order.builder().type(OrderType.SELL).price(-7.0).quantity(30.0).currencyPair(DB_CURRENCY_PAIR).build();
        Assert.assertFalse(order.IsValid());
    }
}
