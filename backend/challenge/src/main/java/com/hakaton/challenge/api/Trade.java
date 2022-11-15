package com.hakaton.challenge.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    private int id;
    private int buyOrderId;
    private int sellOrderId;
    private Date createdDateTime;
    private Double price;
    private Double quantity;
}
