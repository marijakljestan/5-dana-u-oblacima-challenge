package com.hakaton.challenge.api;

import com.hakaton.challenge.domain.OrderEntity;
import com.hakaton.challenge.repository.OrderRepository;
import com.hakaton.challenge.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order> processOrder(@RequestBody Order order){
        Order processedOrder = orderService.processOrder(order);
        if(processedOrder == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing order!");

        return new ResponseEntity<>(processedOrder, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //DELETE THIS
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllOrdersAndTrades() {
        orderService.deleteAll();
        return new ResponseEntity<>("All records deleted!", HttpStatus.OK);
    }
}
