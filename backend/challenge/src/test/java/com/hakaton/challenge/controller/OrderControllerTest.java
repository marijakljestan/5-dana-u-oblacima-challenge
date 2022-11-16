package com.hakaton.challenge.controller;

import com.hakaton.challenge.api.Order;
import com.hakaton.challenge.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.nio.charset.Charset;

import static com.hakaton.challenge.constants.OrderConstants.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    private static final String URL_PREFIX = "/order";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void contextLoads() {}

    @org.junit.Test
    public void testProcessOrder() throws Exception {
        Order order = Order.builder().id(DB_ORDER_ID).currencyPair(DB_CURRENCY_PAIR).orderStatus(DB_ORDER_STATUS)
                .createdDateTime(DB_DATE_TIME).filledQuantity(DB_FILLED_QUANTITY).quantity(-250.0).price(DB_PRICE).build();

        String orderJson = TestUtil.convertToJson(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(new URI(URL_PREFIX)).content(orderJson).contentType(contentType);

        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }
}
