-- Dodanie numeru telefonu do użytkownika
ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);

-- Dodanie tabeli shipping_address
CREATE TABLE shipping_address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255),
    house_number VARCHAR(50),
    apartment_number VARCHAR(50),
    postal_code VARCHAR(20),
    city VARCHAR(100)
);

--Zmiany w tabeli orders: dane kontaktowe i relacje
ALTER TABLE orders ADD COLUMN email VARCHAR(255);
ALTER TABLE orders ADD COLUMN phone_number VARCHAR(20);
ALTER TABLE orders ADD COLUMN guest_name VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address_id BIGINT;

ALTER TABLE orders
    ADD CONSTRAINT fk_shipping_address
        FOREIGN KEY (shipping_address_id)
            REFERENCES shipping_address(id)
            ON DELETE CASCADE;

ALTER TABLE orders
    ADD CONSTRAINT fk_order_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE SET NULL;

--Dodanie tabeli addresses
CREATE TABLE addresses (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255),
    house_number VARCHAR(50),
    apartment_number VARCHAR(50),
    postal_code VARCHAR(20),
    city VARCHAR(100),
    type VARCHAR(50),
    user_id BIGINT,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
