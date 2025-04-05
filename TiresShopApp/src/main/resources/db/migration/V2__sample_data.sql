-- Role użytkowników
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

-- Użytkownicy
INSERT INTO users (username, email, password, enabled) VALUES
                                                           ('admin', 'admin@tires.pl', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFXTfBv6Jz38QsA6K9JbX6N4s.W1X3eK', true),
                                                           ('uzytkownik', 'uzytkownik@tires.pl', '$2a$10$7QYk0Sm0T0e9VQShZ2P0WOmz0WcYfnbI1ZW1z1DlHBoE9fXxTdiZy', true);

-- Przypisanie ról
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin' AND r.name = 'ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'uzytkownik' AND r.name = 'USER';

-- Produkty

-- Opony
INSERT INTO product (name, price, description, stock, type) VALUES
                                                                ('Opona zimowa 205/55R16', 320.00, 'Doskonała przyczepność na śniegu i deszczu', 12, 'TIRE'),
                                                                ('Opona letnia 195/65R15', 250.00, 'Wysoka wydajność w suchych warunkach', 10, 'TIRE');

INSERT INTO tire (id, season, size)
SELECT id, 'WINTER', '205/55R16' FROM product WHERE name = 'Opona zimowa 205/55R16';
INSERT INTO tire (id, season, size)
SELECT id, 'SUMMER', '195/65R15' FROM product WHERE name = 'Opona letnia 195/65R15';

-- Felga
INSERT INTO product (name, price, description, stock, type) VALUES
    ('Felga aluminiowa 17" 5x112', 420.00, 'Nowoczesna felga do aut sportowych', 6, 'RIM');

INSERT INTO rim (id, material, size, bolt_pattern)
SELECT id, 'ALUMINIUM', '17"', '5x112' FROM product WHERE name = 'Felga aluminiowa 17" 5x112';

-- Akcesoria
INSERT INTO product (name, price, description, stock, type) VALUES
                                                                ('Czujnik ciśnienia TPMS', 120.00, 'Czujnik monitorujący ciśnienie w oponach', 20, 'ACCESSORY'),
                                                                ('Komplet śrub do kół', 60.00, 'Zestaw 4 śrub wysokiej jakości', 15, 'ACCESSORY');

INSERT INTO accessory (id, type)
SELECT id, 'SENSOR' FROM product WHERE name = 'Czujnik ciśnienia TPMS';
INSERT INTO accessory (id, type)
SELECT id, 'BOLT' FROM product WHERE name = 'Komplet śrub do kół';
