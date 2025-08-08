-- Cria a procedure para desativar um cliente que não havia criado antes

CREATE OR REPLACE PROCEDURE desativar_cliente(p_cliente_id UUID)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE clientes SET ativo = FALSE WHERE id = p_cliente_id;
END;
$$;