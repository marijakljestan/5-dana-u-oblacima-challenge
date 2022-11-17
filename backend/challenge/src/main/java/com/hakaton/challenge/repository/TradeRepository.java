package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
    @Query(value = "select t from TradeEntity t where t.sellOrderId = ?1 or t.buyOrderId = ?1")
    List<TradeEntity> fetchTradesByOrder(int orderId);
}
