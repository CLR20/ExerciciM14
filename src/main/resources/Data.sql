DROP TABLE IF EXISTS collars;
DROP TABLE if EXISTS shops;

CREATE TABLE shops (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  capacity int NOT NULL,
  PRIMARY KEY (id));

INSERT INTO shops (id, name, capacity) VALUES (DEFAULT, 'Shop 1', 10);
INSERT INTO shops (id, name, capacity) VALUES (DEFAULT, 'Shop 2', 5);  

CREATE TABLE `collars` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `entry_date` varchar(255) NOT NULL,
  `shop_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`));

INSERT INTO collars (id, name, creator, price, entry_date, shop_id) VALUES (DEFAULT, 'Collar 1', 'Creator 1', 10, '01-01-2021', 1);
INSERT INTO collars (id, name, creator, price, entry_date, shop_id) VALUES (DEFAULT, 'Collar 2', null, 12, '01-01-2021', 1);
INSERT INTO collars (id, name, creator, price, entry_date, shop_id) VALUES (DEFAULT, 'Collar 3', 'Creator 2', 10, '01-01-2021', 2);
INSERT INTO collars (id, name, creator, price, entry_date, shop_id) VALUES (DEFAULT, 'Collar 4', 'Creator 3', 10, '01-03-2021', 1);