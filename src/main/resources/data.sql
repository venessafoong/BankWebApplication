INSERT INTO bank_account (balance, number)
VALUES (1000, 11111);

INSERT INTO bank_account (balance, number)
VALUES (2000, 22222);

INSERT INTO bank_account (balance, number)
VALUES (3000, 33333);

INSERT INTO bank_account (balance, number)
VALUES (4000, 44444);

INSERT INTO user (birth_date, acc_number, email, password, phone_number, user_id, username)
VALUES ("2022-08-01", 11111, "real_user@gmail.com", "123", "+85211223344", "M875458(3)","realuser");

INSERT INTO user (birth_date, acc_number, email, password, phone_number, user_id, username)
VALUES ("1997-07-07", 22222, "agent007@gmail.com", "123", "+852007007","A00000(7)","jamesbond");

INSERT INTO user (birth_date, acc_number, email, password, phone_number, user_id, username)
VALUES ("2000-01-01", 33333, "generic@gmail.com", "123", "+85212345678","M123456(7)","johndoe");

INSERT INTO user (birth_date, acc_number, email, password, phone_number, user_id, username)
VALUES ("2002-02-02", 44444, "yosemite@gmail.com", null, "+6591234567","S1234567G",null);

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.03, 5000, -7500, "2025-05-01", 50, "2023-08-01", 0, 4269577723839911, "M875458(3)");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.07, 7000, -7777, "2027-07-07", 50, "2023-07-07", 0, 7376584213831312, "A00000(7)");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.07, 7000, -235, "2026-03-17", 50, "2023-07-07", 0, 4269535443839913, "A00000(7)");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.03, 5000, -1234, "2023-12-23", 50, "2023-07-07", 0, 9185599213831314, "M123456(7)");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.03, 5000, -6789, "2025-12-23", 50, "2023-07-07", 0, 5678234213831314, "M123456(7)");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.03, 5000, -500, "2024-12-23", 50, "2023-07-07", 0, 8781234213831314, "S1234567G");

INSERT INTO credit_card (cash_back_rate, credit_limit, current_balance, expiration_date, minimum_amount, payment_due_date, statement_amount, credit_card_number, user_user_id)
VALUES (0.03, 5000, -400, "2025-12-23", 50, "2023-07-07", 0, 8878234213831314, "S1234567G");

INSERT INTO merchant (id, merchant_category_code, name)
VALUES (1, 4201, "SHOPEE SINGAPORE PTE LTD");

INSERT INTO merchant (id, merchant_category_code, name)
VALUES (2, 1125, "APPLE COMPUTER, INC.");

INSERT INTO merchant (id, merchant_category_code, name)
VALUES (3, 5698, "ALIBABA GROUP HOLDING LIMITED");

INSERT into credit_card_transaction (charge_amount, credit_card_number, credit_card_transaction_id, date_time, type)
values (-9, 4269535443839913, 999999, "2023-06-12 12:00:00", "cashback");

INSERT into credit_card_transaction (charge_amount, credit_card_number, credit_card_transaction_id, date_time, type, merchant)
values (300, 4269535443839913, 99999, "2023-06-12 12:00:00", "purchase", "SHOPEE SINGAPORE PTE LTD");