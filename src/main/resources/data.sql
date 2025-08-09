--Hinzufügen der Produkte - IDs werden automatisch generiert
--T-SHIRT
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Shirt 1', 'Cooles Shirt', 29.99, 10);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Shirt 2', 'Tolles Shirt', 24.99, 15);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Shirt 3', 'Super Shirt', 34.99, 20);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Shirt 4', 'Super Weiss', 34.99, 10);
--USB
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('USB 1', 'Farbe ROT - speichert viele Daten - 1 GB', 14.99, 40);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('USB 2', 'Farbe BLAU - schnell und zuverlässig - 2 GB', 19.99, 50);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('USB 3', 'Farbe SCHWARZ - super schneller Speicher - 8 GB', 29.99, 25);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('USB 4', 'Farbe WEISS - stylisch und robust - 16 GB', 34.99, 20);

--Bauchtasche
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Bauchtasche 1', 'Farbe ROT - praktisch und stylisch, passt für alle Gelegenheiten', 14.99, 40);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Bauchtasche 2', 'Farbe BLAU - robust und wasserfest, ideal für unterwegs', 19.99, 50);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Bauchtasche 3', 'Farbe SCHWARZ - extra sichere Tasche mit Reißverschluss', 29.99, 25);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Bauchtasche 4', 'Farbe WEISS - elegant und leicht, ideal für den täglichen Gebrauch', 34.99, 20);


--Regenschirm
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Regenschirm Rot', 'Farbe ROT - hochwertiger Schutz vor Regen, kompakt und leicht', 14.99, 30);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Regenschirm Blau', 'Farbe BLAU - stilvoll und robust, ideal für jede Gelegenheit', 19.99, 40);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Regenschirm Schwarz', 'Farbe SCHWARZ - klassischer Look, windfest und langlebig', 25.99, 20);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Regenschirm Weiß', 'Farbe WEISS - modern und elegant, perfekt für den Alltag', 29.99, 15);

--Tasse
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Tasse Rot', 'Farbe ROT - Hochwertige Keramiktasse, perfekt für Kaffee und Tee', 9.99, 50);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Tasse Blau', 'Farbe BLAU - Robuste Keramiktasse mit modernem Design', 11.99, 40);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Tasse Schwarz', 'Farbe SCHWARZ - Stilvolle Tasse für umfangreiche Nutzung', 12.99, 30);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Tasse Weiß', 'Farbe WEISS - Elegante und zeitlose Tasse für den Alltag', 10.99, 45);

--Tasse
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Armband Rot', 'Farbe ROT - Stilvolles und elegantes Armband aus hochwertigen Materialien', 14.99, 50);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Armband Blau', 'Farbe BLAU - Modernes Armband mit robustem Design, passend für jede Gelegenheit', 16.99, 40);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Armband Weiß', 'Farbe WEISS - Zeitloses und raffiniertes Armband, ideal als Geschenk', 15.99, 45);
INSERT INTO products (product_name, product_description, product_price, product_quantity)
VALUES ('Armband Schwarz', 'Farbe SCHWARZ - Klassisches und vielseitiges Armband für den täglichen Gebrauch', 18.99, 30);




--CATEGORIE SHIRT
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'GDN T-Shirt', 'AC');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'GDN T-Shirt', 'AC');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'GDN T-Shirt', 'AC');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'GDN T-Shirt', 'AC');


--CATEGORIE USB
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'USB - Massenspeicher', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'USB - Massenspeicher', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'USB - Massenspeicher', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'USB - Massenspeicher', 'EX');


--CATEGORIE BAUCHTASCHE
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'BAUCHTASCHE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'BAUCHTASCHE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'BAUCHTASCHE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('CLOTHING', 'BAUCHTASCHE', 'EX');



--CATEGORIE REGENSCHIRM
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'Regenschirm', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'Regenschirm', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'Regenschirm', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'Regenschirm', 'EX');


--CATEGORIE TASSE
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'TASSE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'TASSE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'TASSE', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'TASSE', 'EX');


--CATEGORIE ARMBAND
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'ARMBAND', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'ARMBAND', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'ARMBAND', 'EX');
INSERT INTO categories (category_name, category_description, department)
VALUES ('EXTRAS', 'ARMBAND', 'EX');




INSERT INTO payments (payment_date, payment_amount, payment_method, payment_status)
VALUES (DATE '2021-01-01', 100.00, 'CC', 'P');
INSERT INTO payments (payment_date, payment_amount, payment_method, payment_status)
VALUES (DATE '2021-02-01', 150.00, 'CC', 'P');
INSERT INTO payments (payment_date, payment_amount, payment_method, payment_status)
VALUES (DATE '2021-03-01', 200.00, 'PP', 'O');
INSERT INTO payments (payment_date, payment_amount, payment_method, payment_status)
VALUES (DATE '2021-04-01', 250.00, 'WT', 'P');
INSERT INTO payments (payment_date, payment_amount, payment_method, payment_status)
VALUES (DATE '2021-05-01', 300.00, 'PP', 'O');

--Product IDs werden jetzt automatisch generiert
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (1, 0, '/images/t-shirt_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (2, 0, '/images/t-shirt_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (3, 0, '/images/t-shirt_schwarz.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (4, 0, '/images/t-shirt_weiss.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (5, 0, '/images/usb_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (6, 0, '/images/usb_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (7, 0, '/images/usb_schwarz.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (8, 0, '/images/usb_weiss.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (9, 0, '/images/bauchtasche_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (10, 0, '/images/bauchtasche_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (11, 0, '/images/bauchtasche_grau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (12, 0, '/images/bauchtasche_weiss.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (13, 0, '/images/regenschirm_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (14, 0, '/images/regenschirm_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (15, 0, '/images/regenschirm_schwarz.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (16, 0, '/images/regenschirm_weiss.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (17, 0, '/images/tasse_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (18, 0, '/images/tasse_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (19, 0, '/images/tasse_schwarz.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (20, 0, '/images/tasse_gelb.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (21, 0, '/images/armband_rot.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (22, 0, '/images/armband_blau.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (23, 0, '/images/armband_weiss.jpg');
INSERT INTO product_image (product_id, order_idx, image_name)
VALUES (24, 0, '/images/armband_schwarz.jpg');


INSERT INTO shopping_cart(id)
VALUES (1);

INSERT INTO shopping_cart_products(product_id, shopping_cart_id)
VALUES (1,1);

INSERT INTO countries (city_name, iso2code, area_code)
VALUES ('Vienna', 'AT', 43);
INSERT INTO countries (city_name, iso2code, area_code)
VALUES ('Graz', 'AT', 45);
INSERT INTO countries (city_name, iso2code, area_code)
VALUES ('Linz', 'AT', 33);
INSERT INTO countries (city_name, iso2code, area_code)
VALUES ('Salzburg', 'AT', 41);
INSERT INTO countries (city_name, iso2code, area_code)
VALUES ('Tirol', 'AT', 51);

-- INSERT INTO orders (quantity, order_date, status, total_price, shipping_date, delivery_date)
-- VALUES (50, '2021-01-01',  'P', 100.00, '2021-01-02',  '2021-01-03');
-- INSERT INTO orders (quantity, order_date, status, total_price, shipping_date, delivery_date)
-- VALUES (40, '2021-02-01',  'C', 80.00, '2021-02-02',  '2021-02-03');
-- INSERT INTO orders ( quantity, order_date, status, total_price, shipping_date, delivery_date)
-- VALUES (60, '2021-03-01',  'C', 120.00, '2021-03-02', '2021-03-03');
-- INSERT INTO orders (quantity, order_date, status, total_price, shipping_date, delivery_date)
-- VALUES (30, '2021-04-01', 'S', 70.00, '2021-04-02',  '2021-04-03');
-- INSERT INTO orders ( quantity, order_date, status, total_price, shipping_date, delivery_date)
-- VALUES (70, '2021-05-01',  'D', 140.00, '2021-05-02',  '2021-05-03');
--
--
--
-- INSERT INTO orderitems ( quantity, price, product_id)
-- VALUES ( 15, 12.50,1);
-- INSERT INTO orderitems ( quantity, price, product_id)
-- VALUES ( 45, 19.99,2);
-- INSERT INTO orderitems ( quantity, price, product_id)
-- VALUES ( 20, 5.99,3);
-- INSERT INTO orderitems ( quantity, price, product_id)
-- VALUES (  60, 50.00,4);
-- INSERT INTO orderitems ( quantity, price, product_id)
-- VALUES ( 100, 99.99,5);


INSERT INTO users (first_name, last_name, email_Address, username, encoded_password, user_role, street, house_number, door_number, city, postal_code, address_type, phone_country_code, phone_area_code, phone_serial_number, phone_extension)
VALUES ( 'MAX', 'MusterMann', 'max@mustermann.at', 'maximu', '$2a$10$WiQPUOmniCp76MLYlabQOOgylXY2hTgrVFSJd6cLR9V/wIlGNqOGW', 'A', 'Main Street', 1, 1, 'Vienna', 1010, 'BI', 43, 1, '1234567', 1);
INSERT INTO users ( first_name, last_name, email_Address, username, encoded_password, user_role, street, house_number, door_number, city, postal_code, address_type, phone_country_code, phone_area_code, phone_serial_number, phone_extension)
VALUES ('ANNA', 'BeispielFrau', 'anna@mustermann.at', 'annam', '$2a$10$WiQPUOmniCp76MLYlabQOOgylXY2hTgrVFSJd6cLR9V/wIlGNqOGW','G', 'Baker Street', 5, 1, 'London', 5678, 'BI', 44, 2, '7654321', 1 );
INSERT INTO users ( first_name, last_name, email_Address, username, encoded_password, user_role, street, house_number, door_number, city, postal_code, address_type, phone_country_code, phone_area_code, phone_serial_number, phone_extension)
VALUES ('LUCAS', 'BeispielMann', 'lucas@mustermann.at', 'lukky', '$2a$10$WiQPUOmniCp76MLYlabQOOgylXY2hTgrVFSJd6cLR9V/wIlGNqOGW', 'O', 'King Street', 3, 3, 'Berlin', 1111, 'DE', 49, 1, '9876543', 2 );
INSERT INTO users ( first_name, last_name, email_Address, username, encoded_password, user_role, street, house_number, door_number, city, postal_code, address_type, phone_country_code, phone_area_code, phone_serial_number, phone_extension)
VALUES ('ELLA', 'MusterFrau', 'ella@mustermann.at', 'ellas', '$2a$10$WiQPUOmniCp76MLYlabQOOgylXY2hTgrVFSJd6cLR9V/wIlGNqOGW', 'G', 'Green Lane', 10, 2, 'Zurich', 8001, 'PR', 41, 3, '5432167', 1 );
INSERT INTO users ( first_name, last_name, email_Address, username, encoded_password, user_role, street, house_number, door_number, city, postal_code, address_type, phone_country_code, phone_area_code, phone_serial_number, phone_extension)
VALUES ( 'JULIA', 'BeispielFrau', 'jullia@mustermann.at', 'julma', '$2a$10$WiQPUOmniCp76MLYlabQOOgylXY2hTgrVFSJd6cLR9V/wIlGNqOGW', 'C', 'Spengergasse', 8, 1, 'Vienna', 2000, 'BI', 31, 1, '1122334', 1);





INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (1,1,1);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (2,2,2);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (3,3,3);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (4,4,4);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (5,5,5);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (6,6,6);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (7,7,7);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (8,8,8);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (9,9,9);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (10,10,10);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (11,11,11);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (12,12,12);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (13,13,13);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (14,14,14);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (15,15,15);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (16,16,16);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (17,17,17);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (18,18,18);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (19,19,19);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (20,20,20);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (21,21,21);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (22,22,22);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (23,23,23);
INSERT INTO product_categories (product_id, category_idx, categories_id)
VALUES (24,24,24);




INSERT INTO orders_order_items (order_items_id, orderitems_idx, order_id)
VALUES (1,1,1);