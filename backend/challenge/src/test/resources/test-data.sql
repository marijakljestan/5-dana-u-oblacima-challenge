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