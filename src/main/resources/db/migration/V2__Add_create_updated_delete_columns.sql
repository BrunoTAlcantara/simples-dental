-- Adicionar campos de auditoria as tabelas

ALTER TABLE categories
ADD COLUMN created_at TIMESTAMPTZ DEFAULT now(),
ADD COLUMN updated_at TIMESTAMPTZ,
ADD COLUMN deleted_at TIMESTAMPTZ;

ALTER TABLE products
ADD COLUMN created_at TIMESTAMPTZ DEFAULT now(),
ADD COLUMN updated_at TIMESTAMPTZ,
ADD COLUMN deleted_at TIMESTAMPTZ;