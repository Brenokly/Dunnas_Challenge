-- Adiciona a coluna 'ativo' para implementar o soft delete

ALTER TABLE clientes
ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;