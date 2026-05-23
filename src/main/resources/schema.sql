CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL CHECK (TRIM(name) <> '')
);

CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL CHECK (TRIM(name) <> ''),
  description TEXT,
  price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
  stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
  category_id INTEGER NOT NULL,
  CONSTRAINT fk_category
    FOREIGN KEY (category_id)
    REFERENCES category(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);
