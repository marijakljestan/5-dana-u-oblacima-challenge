package com.hakaton.challenge.api;

import com.hakaton.challenge.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> processOrder(@RequestBody Order order){
        if(!order.IsValid())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order values!");

        Order processedOrder = orderService.ProcessOrder(order);
        return new ResponseEntity<>(processedOrder, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.FetchOrderByIdWithTrades(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllOrdersAndTrades() {
        orderService.deleteAll();
        return new ResponseEntity<>("All records deleted!", HttpStatus.OK);
    }
}
