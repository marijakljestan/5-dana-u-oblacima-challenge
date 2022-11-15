package com.hakaton.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trade_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeEntity {

    @Id
    @SequenceGenerator(name = "tradeSeqGen", sequenceName = "tradeSeqGen", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tradeSeqGen")
    @Column(name="trade_id", unique=true, nullable=false)
    private int id;

    @Column(nullable = false)
    private int buyOrderId;

    @Column(nullable = false)
    private int sellOrderId;

    @Column(nullable = false)
    private Date createdDateTime;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double quantity;
}
