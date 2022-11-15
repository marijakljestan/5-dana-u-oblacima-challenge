package com.hakaton.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
    @Column(name="order_id", unique=true, nullable=false)
    private int id;

    @Column(nullable = false)
    private String currencyPair = "BTCUSD";

    @Column(nullable = false)
    private Date createdDateTime;

    @Column(nullable = false)
    private OrderType type;

    @Column(nullable = false, precision = 2)
    private Double price;

    @Column(nullable = false, precision = 2)
    private Double quantity;

    @Column(nullable = false, precision = 2)
    private Double filledQuantity;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TradeEntity> trades;

}
