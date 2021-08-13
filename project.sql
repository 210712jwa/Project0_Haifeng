DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS client;
TRUNCATE TABLE account;
TRUNCATE TABLE client;

CREATE TABLE client (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	first_name VARCHAR(255) NOT NULL CHECK(LENGTH(first_name) > 0),
	last_name VARCHAR(255) NOT NULL CHECK(LENGTH(last_name) > 0)	
);

INSERT INTO client (first_name, last_name)
VALUES
('April', 'Apple'),
('Bob', 'Banana'),
('Chloe', 'Cherry'),
('Danny', 'Dates'),
('Ella', 'Elderberry');

CREATE TABLE account (
	account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	account_balance NUMERIC (38, 2) DEFAULT 0,
	account_type VARCHAR(255),
	client_id INTEGER NOT NULL,
	FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE 
);


INSERT INTO account (account_balance, account_type, client_id)
VALUES
(499.99, 'CHECKING', 1),
(500.00, 'SAVING', 1),
(500.01, 'CHECKING', 1),
(1999.99, 'SAVING', 1),
(2000.00, 'CHECKING', 1),
(2000.01, 'SAVING', 1),
(300.45, 'SAVING', 2),
(40000.56, 'CHECKING', 2),
(500.67,' SAVING', 3),
(5200.23,' CHECKING', 4),
(5200.23,' SAVING', 4);


SELECT * FROM client;
SELECT * FROM account;

UPDATE account SET account_balance=1000, account_type='SAVING' WHERE account_id=1 AND client_id=1


SELECT account_balance, client_id
FROM ( SELECT 100 as account_balance , 4 as client_id) as temp
WHERE EXISTS (SELECT client_id FROM client WHERE client_id= 4)
 
	


SELECT * 
FROM account a 
WHERE client_id = 1 AND (account_balance BETWEEN 1200 and 5000);

SELECT client.id, client.first_name, client.last_name, account.account_id, account.account_balance, account.account_type FROM client INNER JOIN account ON client.id = account.client_id WHERE client.id=1


SELECT * FROM project0.account WHERE client_id=1 AND (account_balance >= 500 AND account_balance <= 2000);

