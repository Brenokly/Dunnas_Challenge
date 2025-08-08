-- V5__create_function_reativar_cliente.sql
CREATE OR REPLACE FUNCTION reativar_cliente(p_cliente_id UUID)
RETURNS VOID AS $$
BEGIN
    UPDATE clientes SET ativo = TRUE WHERE id = p_cliente_id;
END;
$$ LANGUAGE plpgsql;