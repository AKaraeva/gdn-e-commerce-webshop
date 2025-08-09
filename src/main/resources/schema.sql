-- -- Drop foreign key constraints
ALTER TABLE orderitems DROP CONSTRAINT IF EXISTS FK_orderitems_products;
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_orders_payments;
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_orders_users;
ALTER TABLE product_categories DROP CONSTRAINT IF EXISTS FK_product_categories_products;
ALTER TABLE product_categories DROP CONSTRAINT IF EXISTS FK_product_categories_categories;
ALTER TABLE product_image DROP CONSTRAINT IF EXISTS FK_product_image_products;
ALTER TABLE shopping_cart_products DROP CONSTRAINT IF EXISTS FK_shopping_cart_products_shopping_cart;
ALTER TABLE shopping_cart_products DROP CONSTRAINT IF EXISTS FK_shopping_cart_products_products;
ALTER TABLE user_addresses DROP CONSTRAINT IF EXISTS FK_user_addresses_users;
ALTER TABLE user_addresses DROP CONSTRAINT IF EXISTS FK_user_addresses_countries;


-- create sequence categories_seq start with 100 increment by 1;
-- create sequence countries_seq start with 100 increment by 1;
-- create sequence orderitems_seq start with 100 increment by 1;
-- create sequence orders_seq start with 100 increment by 1;
-- create sequence payments_seq start with 100 increment by 1;
-- create sequence products_seq start with 100 increment by 1;
-- create sequence shopping_cart_seq start with 100 increment by 1;
-- create sequence users_seq start with 100 increment by 1;

CREATE SEQUENCE IF NOT EXISTS categories_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS countries_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS orderitems_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS orders_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS payments_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS products_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS shopping_cart_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 100 INCREMENT BY 1;

DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS orderitems;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS orders_order_items;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS product_categories;
DROP TABLE IF EXISTS product_image;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS shopping_cart_products;
DROP TABLE IF EXISTS user_addresses;
DROP TABLE IF EXISTS users; --Hinzugefügt, da in schema.sql vorhanden

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            category_name VARCHAR(255),
                            category_description VARCHAR(255),
                            department CHAR(2) CHECK (department IN ('AC', 'CL', 'EX'))
);

CREATE TABLE countries (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           city_name VARCHAR(255),
                           iso2code VARCHAR(2),
                           area_code INT CHECK (area_code BETWEEN 1 AND 999)
);

CREATE TABLE IF NOT EXISTS orderitems (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            quantity INT CHECK (quantity >= 1),
                            price DOUBLE NOT NULL CHECK (price >= 1),
                            product_id INT
);



CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                       -- quantity INT CHECK (quantity >= 1),
                        order_date DATE,
                        status CHAR(1) CHECK (status IN ('P', 'S', 'D', 'C')),
                        total_price DOUBLE,
                        shipping_date DATE,
                        delivery_date DATE,
                        payment_id INT,
                        user_id INT
);

CREATE TABLE orders_order_items (
                                    orderitems_idx INT NOT NULL,
                                    order_items_id INT NOT NULL UNIQUE,
                                    order_id INT NOT NULL,
                                    PRIMARY KEY (order_id, orderitems_idx)
);

CREATE TABLE payments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          payment_date DATE,
                          payment_amount DOUBLE NOT NULL,
                          payment_method VARCHAR(255),
                          payment_status CHAR(1) CHECK (payment_status IN ('O', 'P'))
);

CREATE TABLE product_categories (
                                    product_id INT NOT NULL,
                                    category_idx INT NOT NULL,
                                    categories_id INT NOT NULL,
                                    PRIMARY KEY (product_id, category_idx)
);

CREATE TABLE product_image (
                               product_id INT NOT NULL,
                               order_idx INT NOT NULL,
                               image_name VARCHAR(255),
                               PRIMARY KEY (product_id, order_idx)
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          product_name VARCHAR(255),
                          product_description VARCHAR(255),
                          product_price INT NOT NULL,
                          product_quantity INT
);

CREATE TABLE shopping_cart (
                               id INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE shopping_cart_products (
                                        shopping_cart_id INT NOT NULL,
                                        product_id INT NOT NULL,
                                        UNIQUE (shopping_cart_id, product_id)
);

CREATE TABLE user_addresses (
                                user_id INT NOT NULL,
                                order_idx INT NOT NULL,
                                street VARCHAR(255),
                                city VARCHAR(255),
                                house_number INT,
                                door_number INT,
                                postal_code INT,
                                country_id INT NOT NULL,
                                address_type CHAR(2) CHECK (address_type IN ('BI', 'DE', 'PR')) NOT NULL,
                                PRIMARY KEY (user_id, order_idx)
);

CREATE TABLE users ( --Hinzugefügt, da in schema.sql vorhanden
                       id INT AUTO_INCREMENT PRIMARY KEY,
    -- weitere Spalten für die User-Tabelle hinzufügen
                        --Beispiel einer Spalte

                       first_name          varchar(255),
                       last_name           varchar(255),
                       email_Address        VARCHAR(255) UNIQUE NOT NULL,
                       username            varchar(255),
                       encoded_password    varchar(255) not null,
                       user_role           char(1) check (user_role in('A', 'C', 'G','O')) not null,
                       street              varchar(255),
                       house_number        INT,
                       door_number         INT,
                       city                varchar(255),
                       postal_code         INT,
                       address_type        char(2) check(address_type in  ('BI', 'DE', 'PR')),
                       phone_country_code  INT check (phone_country_code between 1 and 999),
                       phone_area_code     INT,
                       phone_serial_number varchar(255),
                       phone_extension     INT,
                       country_id          INT
);
ALTER TABLE orderitems ADD CONSTRAINT FK_orderitems_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE SET NULL;
ALTER TABLE orders ADD CONSTRAINT FK_orders_payments FOREIGN KEY (payment_id) REFERENCES payments (id);
ALTER TABLE orders ADD CONSTRAINT FK_orders_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE product_categories ADD CONSTRAINT FK_product_categories_products FOREIGN KEY (product_id) REFERENCES products (id);
ALTER TABLE product_categories ADD CONSTRAINT FK_product_categories_categories FOREIGN KEY (categories_id) REFERENCES categories (id);
ALTER TABLE product_image ADD CONSTRAINT FK_product_image_products FOREIGN KEY (product_id) REFERENCES products (id);
ALTER TABLE shopping_cart_products ADD CONSTRAINT FK_shopping_cart_products_shopping_cart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (id);
ALTER TABLE shopping_cart_products ADD CONSTRAINT FK_shopping_cart_products_products FOREIGN KEY (product_id) REFERENCES products (id)ON DELETE CASCADE;
ALTER TABLE user_addresses ADD CONSTRAINT FK_user_addresses_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_addresses ADD CONSTRAINT FK_user_addresses_countries FOREIGN KEY (country_id) REFERENCES countries (id);
