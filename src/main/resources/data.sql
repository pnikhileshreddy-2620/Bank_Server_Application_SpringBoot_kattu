INSERT INTO CUSTOMERS (CUSTOMER_ID, NAME, EMAIL,PASSWORD) VALUES (1, 'John Doe', 'john.doe@example.com','john123');
INSERT INTO CUSTOMERS (CUSTOMER_ID, NAME, EMAIL,PASSWORD) VALUES (2, 'Jane Smith', 'jane.smith@example.com','jane123');
INSERT INTO CUSTOMERS (CUSTOMER_ID, NAME, EMAIL,PASSWORD) VALUES (3, 'Bob Johnson', 'bob.johnson@example.com','bob123');
INSERT INTO accounts (account_number, account_type, current_balance, customer_id) VALUES (1, 'Savings', 1000.00, 1);
INSERT INTO accounts (account_number, account_type, current_balance, customer_id) VALUES (2, 'Checking', 500.00, 2);
INSERT INTO accounts (account_number, account_type, current_balance, customer_id) VALUES (3, 'Fixed Deposit', 5000.00, 3);
INSERT INTO transactions (transaction_id, amount, transaction_date, transaction_type, account_number) VALUES (12345, 500.00, '2023-02-03', 'deposit', 1);
INSERT INTO transactions (transaction_id, amount, transaction_date, transaction_type, account_number) VALUES (12346, 500.00, '2023-02-04', 'deposit', 2);
INSERT INTO transactions (transaction_id, amount, transaction_date, transaction_type, account_number) VALUES (12347, 500.00, '2023-02-05', 'deposit', 3);
INSERT INTO ADMIN(NAME,USERNAME,PASSWORD) VALUES ('Parker','parker@gmail.com','icici@123');
INSERT INTO ADMIN(NAME,USERNAME,PASSWORD) VALUES ('Tony','tony@gmail.com','hdfc@123');
INSERT INTO ADMIN(NAME,USERNAME,PASSWORD) VALUES ('Thor','thor@gmail.com','aven@123');









