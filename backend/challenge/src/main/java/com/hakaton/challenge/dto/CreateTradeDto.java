package com.hakaton.challenge.dto;

import com.hakaton.challenge.domain.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTradeDto {
    private OrderEntity newOrder;
    private OrderEntity existingOrder;
    private double quantity;
}

