package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}
