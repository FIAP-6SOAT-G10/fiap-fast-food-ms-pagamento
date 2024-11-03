CREATE TABLE IF NOT EXISTS "payments"."payment"
(
  id varchar(36) not null,
  external_id varchar(100) not null,
  payer varchar(100) not null,
  payment_amount decimal(6, 2) not null,
  payment_date timestamp,
  payment_request_date timestamp not null,
  payment_status varchar(30) not null
);