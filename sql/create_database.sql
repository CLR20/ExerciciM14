CREATE DATABASE whitecollar;

CREATE TABLE shops (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  capacity int NOT NULL,
  PRIMARY KEY (id));
  
CREATE TABLE `collars` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `entry_date` varchar(255) NOT NULL,
  `shop_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`));