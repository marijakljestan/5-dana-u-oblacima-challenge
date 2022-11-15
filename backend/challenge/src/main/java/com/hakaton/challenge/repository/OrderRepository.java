package com.hakaton.challenge.repository;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query(value = "select o from OrderEntity o where o.orderStatus = 0")
    List<OrderEntity> findActiveOrders();

    @Query(value = "select o from OrderEntity o where o.orderStatus = 0 and o.type = 0 and o.price >= ?1")
    List<OrderEntity> findSuitableBuyOrders(double price);
}
