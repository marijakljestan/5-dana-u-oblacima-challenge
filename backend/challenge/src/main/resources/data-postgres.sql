-- orders
INSERT INTO order_entity (order_id, created_date_time, currency_pair, filled_quantity, order_status, price, quantity, type)
VALUES (nextval('order_seq_gen'), '2022-05-24 16:48:05.591', 'BTCUSD', 0.0, 0, 7.0, 250.0, 0),
       (nextval('order_seq_gen'), '2022-05-24 16:48:05.591', 'BTCUSD', 0.0, 0, 9.0, 100.0, 1),
       (nextval('order_seq_gen'), '2022-05-24 16:48:05.591', 'BTCUSD', 0.0, 0, 10.0, 50.0, 1),
       (nextval('order_seq_gen'), '2022-05-24 16:49:05.591', 'BTCUSD', 0.0, 0, 11.0, 1000.0, 1),
       (nextval('order_seq_gen'), '2022-05-24 16:38:05.591', 'BTCUSD', 0.0, 0, 15.0, 250.0, 1),
       (nextval('order_seq_gen'), '2022-05-24 16:38:05.591', 'BTCUSD', 0.0, 0, 6.0, 800.0, 0),
       (nextval('order_seq_gen'), '2022-05-24 16:38:05.591', 'BTCUSD', 0.0, 0, 5.0, 150.0, 0),
       (nextval('order_seq_gen'), '2022-05-24 16:38:05.591', 'BTCUSD', 0.0, 0, 1.0, 5000.0, 0);

-- trades
INSERT INTO trade_entity (trade_id, buy_order_id, created_date_time, price, quantity, sell_order_id)
VALUES (nextval('trade_seq_gen'), 1, '2022-05-24 16:48:05.591', 100.0, 0.1, 2),
       (nextval('trade_seq_gen'), 1, '2022-07-24 16:48:05.591', 50.0, 0.2, 3);
       --(nextval('trade_seq_gen'), 2, '2022-10-07 13:48:05.591', 1000.0, 0.0, 2),
      --- (nextval('trade_seq_gen'), 2, '2022-10-07 15:47:05.591', 250.0, 0.0, 2);

-- order - trades
INSERT INTO order_entity_trades (order_entity_order_id, trades_trade_id)
VALUES (1, 1),
       (1, 2);