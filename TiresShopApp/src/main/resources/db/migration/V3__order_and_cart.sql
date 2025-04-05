-- Tabela zamówień
CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        status VARCHAR(30) NOT NULL DEFAULT 'CREATED',
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Pozycje zamówienia (order_item)
CREATE TABLE order_item (
                            id SERIAL PRIMARY KEY,
                            order_id INT NOT NULL,
                            product_id INT NOT NULL,
                            quantity INT NOT NULL,
                            price_at_purchase NUMERIC(10,2) NOT NULL,
                            FOREIGN KEY (order_id) REFERENCES orders(id),
                            FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Koszyk użytkownika (tymczasowy, niezapisane zamówienia)
CREATE TABLE cart_item (
                           id SERIAL PRIMARY KEY,
                           user_id INT NOT NULL,
                           product_id INT NOT NULL,
                           quantity INT NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (product_id) REFERENCES product(id)
);
