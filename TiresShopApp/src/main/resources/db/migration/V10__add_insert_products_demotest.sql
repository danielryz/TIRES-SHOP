-- V2__insert_products.sql

-- Insert into product
INSERT INTO product (id, name, price, description, stock, type) VALUES
                                                                    (10, 'Michelin Primacy 4', 299.99, 'Opona letnia premium 205/55R16', 50, 'TIRE'),
                                                                    (11, 'Dunlop Winter Sport 5', 259.99, 'Opona zimowa 195/65R15', 40, 'TIRE'),
                                                                    (12, 'Continental EcoContact 6', 279.99, 'Opona letnia 185/65R15', 30, 'TIRE'),
                                                                    (13, 'Goodyear Vector 4Seasons', 319.99, 'Opona całoroczna 205/60R16', 45, 'TIRE'),
                                                                    (14, 'Pirelli Cinturato P7', 349.99, 'Opona letnia 225/45R17', 35, 'TIRE'),
                                                                    (15, 'Bridgestone Blizzak LM005', 379.99, 'Opona zimowa 215/55R17', 25, 'TIRE'),
                                                                    (16, 'Nokian WR Snowproof', 289.99, 'Opona zimowa 195/60R15', 20, 'TIRE'),
                                                                    (17, 'Vredestein Quatrac Pro', 399.99, 'Opona całoroczna 225/50R17', 40, 'TIRE'),
                                                                    (18, 'Hankook Kinergy 4S2', 279.99, 'Opona całoroczna 215/50R17', 38, 'TIRE'),
                                                                    (19, 'Toyo Proxes Sport', 419.99, 'Opona letnia 245/40R18', 28, 'TIRE'),
                                                                    (20, 'Aluminiowa felga 17"', 499.99, 'Felga aluminiowa 7.0x17 5x112', 60, 'RIM'),
                                                                    (21, 'Stalowa felga 16"', 249.99, 'Felga stalowa 6.5x16 5x114', 70, 'RIM'),
                                                                    (22, 'Aluminiowa felga 18"', 599.99, 'Felga aluminiowa 8.0x18 5x120', 55, 'RIM'),
                                                                    (23, 'Aluminiowa felga 19"', 699.99, 'Felga aluminiowa 8.5x19 5x112', 45, 'RIM'),
                                                                    (24, 'Stalowa felga 15"', 199.99, 'Felga stalowa 6.0x15 4x100', 80, 'RIM'),
                                                                    (25, 'Aluminiowa felga 16"', 399.99, 'Felga aluminiowa 7.0x16 5x108', 65, 'RIM'),
                                                                    (26, 'Stalowa felga 14"', 179.99, 'Felga stalowa 5.5x14 4x98', 85, 'RIM'),
                                                                    (27, 'Aluminiowa felga 17" czarna', 549.99, 'Felga aluminiowa czarna 7.5x17 5x112', 50, 'RIM'),
                                                                    (28, 'Stalowa felga 17"', 299.99, 'Felga stalowa 7.0x17 5x114', 60, 'RIM'),
                                                                    (29, 'Aluminiowa felga 18" srebrna', 649.99, 'Felga aluminiowa srebrna 8.0x18 5x120', 40, 'RIM'),
                                                                    (30, 'Łańcuchy śniegowe', 149.99, 'Łańcuchy na koła do rozmiaru 205/55R16', 30, 'ACCESSORY'),
                                                                    (31, 'Pokrowce na koła', 99.99, 'Zestaw 4 pokrowców na koła', 50, 'ACCESSORY'),
                                                                    (32, 'Podnośnik samochodowy', 129.99, 'Hydrauliczny podnośnik typu żaba', 20, 'ACCESSORY'),
                                                                    (33, 'Zestaw kluczy do kół', 89.99, 'Zestaw 4 kluczy nasadowych', 40, 'ACCESSORY');

-- Insert into tire
INSERT INTO tire (id, season, size) VALUES
                                        (10, 'SUMMER', '205/55R16'),
                                        (11, 'WINTER', '195/65R15'),
                                        (12, 'SUMMER', '185/65R15'),
                                        (13, 'ALL_SEASON', '205/60R16'),
                                        (14, 'SUMMER', '225/45R17'),
                                        (15, 'WINTER', '215/55R17'),
                                        (16, 'WINTER', '195/60R15'),
                                        (17, 'ALL_SEASON', '225/50R17'),
                                        (18, 'ALL_SEASON', '215/50R17'),
                                        (19, 'SUMMER', '245/40R18');

-- Insert into rim
INSERT INTO rim (id, material, size, bolt_pattern) VALUES
                                                       (20, 'aluminum', '17"', '5x112'),
                                                       (21, 'steel', '16"', '5x114'),
                                                       (22, 'aluminum', '18"', '5x120'),
                                                       (23, 'aluminum', '19"', '5x112'),
                                                       (24, 'steel', '15"', '4x100'),
                                                       (25, 'aluminum', '16"', '5x108'),
                                                       (26, 'steel', '14"', '4x98'),
                                                       (27, 'aluminum', '17"', '5x112'),
                                                       (28, 'steel', '17"', '5x114'),
                                                       (29, 'aluminum', '18"', '5x120');

-- Insert into accessory
INSERT INTO accessory (id, type) VALUES
                                     (30, 'CHAINS'),
                                     (31, 'COVERS'),
                                     (32, 'JACK'),
                                     (33, 'TOOLS');

-- Insert into image
INSERT INTO image (id, url, product_id) VALUES
                                            (10, 'https://example.com/images/10.jpg', 10),
                                            (11, 'https://example.com/images/11.jpg', 11),
                                            (12, 'https://example.com/images/12.jpg', 12),
                                            (13, 'https://example.com/images/13.jpg', 13),
                                            (14, 'https://example.com/images/14.jpg', 14),
                                            (15, 'https://example.com/images/15.jpg', 15),
                                            (16, 'https://example.com/images/16.jpg', 16),
                                            (17, 'https://example.com/images/17.jpg', 17),
                                            (18, 'https://example.com/images/18.jpg', 18),
                                            (19, 'https://example.com/images/19.jpg', 19),
                                            (20, 'https://example.com/images/20.jpg', 20),
                                            (21, 'https://example.com/images/21.jpg', 21),
                                            (22, 'https://example.com/images/22.jpg', 22),
                                            (23, 'https://example.com/images/23.jpg', 23),
                                            (24, 'https://example.com/images/24.jpg', 24),
                                            (25, 'https://example.com/images/25.jpg', 25),
                                            (26, 'https://example.com/images/26.jpg', 26),
                                            (27, 'https://example.com/images/27.jpg', 27),
                                            (28, 'https://example.com/images/28.jpg', 28),
                                            (29, 'https://example.com/images/29.jpg', 29),
                                            (30, 'https://example.com/images/30.jpg', 30),
                                            (31, 'https://example.com/images/31.jpg', 31),
                                            (32, 'https://example.com/images/32.jpg', 32),
                                            (33, 'https://example.com/images/33.jpg', 33);
