package com.hakaton.challenge.api;

import com.hakaton.challenge.domain.OrderbookEntity;
import com.hakaton.challenge.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orderbook", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderbookController {

    private final OrderService orderService;

    @GetMapping(value = "/{pair}")
    public ResponseEntity<OrderbookEntity> loadOrderbook(@PathVariable("pair") String pair) {
        OrderbookEntity orderbook = orderService.LoadOrderBook(pair);
        return new ResponseEntity<>(orderbook, HttpStatus.OK);
    }
}
