-- Adiciona constraints na tabela categories
ALTER TABLE categories
  ALTER COLUMN name TYPE VARCHAR(100),
  ALTER COLUMN description TYPE VARCHAR(255),
  ADD CONSTRAINT categories_name_not_empty CHECK (name <> '');

-- Adiciona constraints na tabela products
ALTER TABLE products
  ALTER COLUMN name TYPE VARCHAR(100),
  ADD CONSTRAINT products_name_not_empty CHECK (name <> ''),
  ALTER COLUMN description TYPE VARCHAR(255),
  ADD CONSTRAINT products_price_positive CHECK (price > 0),
  ALTER COLUMN category_id SET NOT NULL;