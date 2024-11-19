INSERT INTO payments.payment
  (internal_payment_id, external_payment_id, external_id, payer, payment_amount, payment_date, payment_request_date, payment_status, payment_method, payment_type)
VALUES
  ('505d4447-3484-43a1-b3bf-678be78e3f2e', 93423751888, '1234567890', 'email@email.com.br', 49.12, '2024-11-14', '2024-11-14', 'paid', 'account_money', 'account_money'),
  ('34cbc15b-3456-438e-9e89-52b16365a9a7', 93422596532, '1234567891', 'email@email.com.br', 15.32, '2024-11-14', '2024-11-14', 'paid', 'account_money', 'account_money'),
  ('6e332372-bc76-4dea-8ab1-6e3808d6f9d9', null, '1234567892', 'email@email.com.br', 10.06, '2024-11-14', '2024-11-14', 'paid', 'account_money', 'account_money'),
  ('c1078c7e-d1f7-4490-853e-f7a4ded0fe3e', null, '1234567893', 'email@email.com.br', 3.99, '2024-11-14', '2024-11-14', 'paid', 'account_money', 'account_money'),
  ('2baa836d-186f-481f-b3a7-341c9c788602', null, '1234567894', 'email@email.com.br', 3.99, '2024-11-14', '2024-11-14', 'paid', 'account_money', 'account_money');