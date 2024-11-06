CREATE TABLE IF NOT EXISTS "payments"."payment"
(
  internal_payment_id varchar(36) not null,
  external_payment_id bigint,
  external_id varchar(100) not null,
  payer varchar(100) not null,
  payment_amount decimal(6, 2) not null,
  payment_date timestamp,
  payment_request_date timestamp not null,
  payment_status varchar(30) not null,
  payment_method varchar(30),
  payment_type varchar(30)
);