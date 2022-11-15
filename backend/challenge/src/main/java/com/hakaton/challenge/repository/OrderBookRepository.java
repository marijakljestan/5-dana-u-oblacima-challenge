package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.OrderBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBookEntity, Integer> {
}
