package com.hakaton.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    private int id;
    private String currencyPair = "BTCUSD";
    private Date createdDateTime;
    private OrderType type;
    private Double price;
    private Double quantity;
    private Double filledQuantity;
    private OrderStatus orderStatus;
    private List<TradeEntity> trades;
}
