INSERT INTO users (id, username, email, password, enabled)
VALUES (3, 'testowy', 'test@tires.pl', '$2a$10$BWmXhNcnOubilK71t9MUBeqPgVLOC3V/ATZ9TPy3VNmsW5b2vy5de', true);
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 1);
