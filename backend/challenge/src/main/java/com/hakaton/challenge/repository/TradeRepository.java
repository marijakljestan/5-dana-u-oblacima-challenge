package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
}
