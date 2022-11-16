package com.hakaton.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderbookEntity {

    private List<OrderbookItem> buyOrders = new ArrayList<>();
    private List<OrderbookItem> sellOrders = new ArrayList<>();

    public static Formatter formatter = new Formatter();


    public void LoadBuyOrder(OrderEntity order){
        if(isBuyOrderWithSamePriceInOrderBook(order))
            accumulateBuyOrder(order);
        else
            addNewBuyOrder(order);
    }

    private Boolean isBuyOrderWithSamePriceInOrderBook(OrderEntity order){
        for(OrderbookItem o : buyOrders){

            if(o.getPrice().equals(order.getPrice()))
                return true;
        }
        return false;
    }


    private void addNewBuyOrder(OrderEntity order){
        OrderbookItem newOrder = OrderbookItem.builder().price(order.getPrice()).quantity(order.getQuantity() - order.getFilledQuantity()).build();
        buyOrders.add(newOrder);
    }

    private void accumulateBuyOrder(OrderEntity order){
        for(OrderbookItem o: buyOrders)
            if(o.getPrice().equals(order.getPrice())){
                Double newQuantity = o.getQuantity() + order.getQuantity() - order.getFilledQuantity();
                Double formatted = BigDecimal.valueOf(newQuantity)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                o.setQuantity(formatted);
                break;
            }
    }

    public void LoadSellOrder(OrderEntity order){
        if(isSellOrderWithSamePriceInOrderBook(order))
            accumulateSellOrder(order);
        else
            addNewSellOrder(order);
    }

    private Boolean isSellOrderWithSamePriceInOrderBook(OrderEntity order){
        for(OrderbookItem o : sellOrders){
            if(o.getPrice().equals(order.getPrice()))
                return true;
        }
        return false;
    }

    private void addNewSellOrder(OrderEntity order){
        OrderbookItem newOrder = OrderbookItem.builder().price(order.getPrice()).quantity(order.getQuantity() - order.getFilledQuantity()).build();
        sellOrders.add(newOrder);
    }

    private void accumulateSellOrder(OrderEntity order){
        for(OrderbookItem o: sellOrders)
            if(o.getPrice().equals(order.getPrice())){
                Double newQuantity = o.getQuantity() + order.getQuantity() - order.getFilledQuantity();
                Double formatted = BigDecimal.valueOf(newQuantity)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                o.setQuantity(formatted);
                break;
            }
    }

}
