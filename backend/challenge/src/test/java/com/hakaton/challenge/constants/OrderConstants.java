package com.hakaton.challenge.constants;

import com.hakaton.challenge.domain.OrderStatus;
import com.hakaton.challenge.domain.OrderType;

import java.util.Date;

public class OrderConstants {

    public static final int DB_ORDER_ID = 1;
    public static final Date DB_DATE_TIME = new Date(2022,10,6);
    public static final String DB_CURRENCY_PAIR= "BTCUSD";
    public static final double DB_FILLED_QUANTITY= 0.0;
    public static final OrderStatus DB_ORDER_STATUS = OrderStatus.OPEN;
    public static final double DB_PRICE= 0.0;
    public static final double DB_QUANTITY= 250.0;
    public static final OrderType DB_TYPE= OrderType.BUY;
}
