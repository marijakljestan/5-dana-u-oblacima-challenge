package com.hakaton.challenge.api;

import com.hakaton.challenge.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;
}
