-- Adição da coluna "ativo" na tabela "produtos" para implementar a "soft delete"

ALTER TABLE produtos
ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;