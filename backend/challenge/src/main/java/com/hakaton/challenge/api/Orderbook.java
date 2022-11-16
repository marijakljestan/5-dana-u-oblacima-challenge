package com.hakaton.challenge.api;

import com.hakaton.challenge.domain.OrderbookItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orderbook {

    private List<OrderbookItem> buyOrders;
    private List<OrderbookItem> sellOrders;
}
