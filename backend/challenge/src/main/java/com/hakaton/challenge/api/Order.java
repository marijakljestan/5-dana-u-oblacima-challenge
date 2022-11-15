package com.hakaton.challenge.api;

import com.hakaton.challenge.domain.OrderStatus;
import com.hakaton.challenge.domain.OrderType;
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
public class Order {

    private int id;
    private String currencyPair = "BTCUSD";
    private Date createdDateTime;
    private OrderType type;
    private Double price;
    private Double quantity;
    private Double filledQuantity;
    private OrderStatus orderStatus;
    private List<Trade> trades;
}
