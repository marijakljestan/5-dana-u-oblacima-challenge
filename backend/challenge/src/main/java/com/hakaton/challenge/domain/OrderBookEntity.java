package com.hakaton.challenge.domain;

import com.hakaton.challenge.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBookEntity {

    private List<OrderDto> buyOrders;
    private List<OrderDto> sellOrders;
}
