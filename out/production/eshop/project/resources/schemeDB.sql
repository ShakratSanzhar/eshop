CREATE DATABASE eshop;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    email    VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(128) UNIQUE NOT NULL,
    role     VARCHAR(128)        NOT NULL
);

CREATE TABLE IF NOT EXISTS user_details
(
    user_id           BIGINT REFERENCES users (id) ON DELETE CASCADE,
    name              VARCHAR(128)        NOT NULL,
    surname           VARCHAR(128)        NOT NULL,
    birthday          DATE                NOT NULL,
    phone             VARCHAR(128) UNIQUE NOT NULL,
    registration_date TIMESTAMP           NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS category
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(256) UNIQUE NOT NULL,
    parent_id INT REFERENCES category (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product
(
    id               BIGSERIAL PRIMARY KEY,
    category_id      INT NOT NULL REFERENCES category (id) ON DELETE CASCADE,
    name             VARCHAR(256) UNIQUE          NOT NULL,
    description      TEXT,
    author           VARCHAR(128)                 NOT NULL,
    publisher        VARCHAR(128)                 NOT NULL,
    publishing_year  DATE                         NOT NULL,
    image            VARCHAR(256)                 NOT NULL,
    price            INT                          NOT NULL,
    remaining_amount INT,
    page_count       INT                          NOT NULL,
    created_at       TIMESTAMP                    NOT NULL
);

CREATE TABLE IF NOT EXISTS cart
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id)  ON DELETE CASCADE,
    price   INT
);

CREATE TABLE IF NOT EXISTS cart_product
(
    cart_id    BIGINT REFERENCES cart (id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    quantity   INT,
    PRIMARY KEY (cart_id, product_id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status     VARCHAR(128)                 NOT NULL,
    price      INT,
    created_at TIMESTAMP                    NOT NULL,
    closed_at  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_product
(
    order_id   BIGINT REFERENCES orders (id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    quantity   INT,
    PRIMARY KEY (order_id, product_id)
);

CREATE TABLE IF NOT EXISTS blacklist
(
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id)
);