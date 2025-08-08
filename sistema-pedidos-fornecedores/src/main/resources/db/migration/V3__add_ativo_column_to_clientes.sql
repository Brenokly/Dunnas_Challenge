-- V3__add_ativo_column_to_clientes.sql
ALTER TABLE clientes
ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;