package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.OrderEntity;
import com.hakaton.challenge.domain.OrderbookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query(value = "select o from OrderEntity o left join fetch o.trades where o.id = ?1")
    OrderEntity fetchWithTrades(int orderId);

    @Query(value = "select o from OrderEntity o where o.orderStatus = 0 and o.currencyPair = ?1")
    List<OrderEntity> findActiveOrders(String pair);

    @Query(value = "select o from OrderEntity o left join fetch o.trades where o.orderStatus = 0 and o.type = 0 and o.price >= ?1 and o.currencyPair = ?2" +
                   "order by o.price desc, o.createdDateTime")
    List<OrderEntity> findSuitableBuyOrders(double price, String currencyPair);

    @Query(value = "select o from OrderEntity o left join fetch o.trades where o.orderStatus = 0 and o.type = 1 and o.price <= ?1 and o.currencyPair = ?2 " +
                    "order by o.price, o.createdDateTime")
    List<OrderEntity> findSuitableSellOrders(double price, String currencyPair);

    @Query(value = "select new com.hakaton.challenge.domain.OrderbookItem(o.price, sum(o.quantity)) " +
                  "from OrderEntity  o where o.orderStatus = 0 and o.type = 0 group by o.price")
    List<OrderbookItem> findActiveBuyOrders();

    @Query(value = "select new com.hakaton.challenge.domain.OrderbookItem(o.price, sum(o.quantity)) " +
            "from OrderEntity  o where o.orderStatus = 0 and o.type = 1 group by o.price")
    List<OrderbookItem> findActiveSellOrders();
}
