ALTER DATABASE tireshop OWNER TO myuser;
-- USERS
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- ROLES
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20) NOT NULL UNIQUE
);

-- USER_ROLES
CREATE TABLE user_roles (
                            user_id INT NOT NULL,
                            role_id INT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- PRODUCT
CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price NUMERIC(10, 2) NOT NULL,
                         description TEXT,
                         stock INT NOT NULL,
                         type VARCHAR(20) NOT NULL
);

-- TIRE
CREATE TABLE tire (
                      id INT PRIMARY KEY,
                      season VARCHAR(20),
                      size VARCHAR(20),
                      FOREIGN KEY (id) REFERENCES product(id)
);

-- RIM
CREATE TABLE rim (
                     id INT PRIMARY KEY,
                     material VARCHAR(50),
                     size VARCHAR(20),
                     bolt_pattern VARCHAR(50),
                     FOREIGN KEY (id) REFERENCES product(id)
);

-- ACCESSORY
CREATE TABLE accessory (
                           id INT PRIMARY KEY,
                           type VARCHAR(30),
                           FOREIGN KEY (id) REFERENCES product(id)
);
