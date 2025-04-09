CREATE TABLE image (
    id SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_image_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE
);
