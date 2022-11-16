package com.hakaton.challenge.services;

import com.hakaton.challenge.constants.OrderConstants;
import com.hakaton.challenge.domain.OrderEntity;
import com.hakaton.challenge.repository.OrderRepository;
import com.hakaton.challenge.service.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTests {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private OrderEntity orderEntityMock;


    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindOrderById() {

        when(orderRepositoryMock.findById(OrderConstants.DB_ORDER_ID)).thenReturn(Optional.of(orderEntityMock));

        OrderEntity orderDb = orderService.findOrderById(OrderConstants.DB_ORDER_ID);

        Assert.assertEquals(orderEntityMock, orderDb);

        verify(orderRepositoryMock, times(1)).findById(OrderConstants.DB_ORDER_ID);
        verifyNoMoreInteractions(orderRepositoryMock);
    }

}
