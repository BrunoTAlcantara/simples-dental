
CREATE INDEX idx_product_name ON products (name);
CREATE INDEX idx_product_category ON products (category_id);


CREATE INDEX idx_category_name ON categories (name);


CREATE UNIQUE INDEX idx_user_email ON users (email);
CREATE INDEX idx_user_role ON users (role);
