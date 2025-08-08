-- Apagar uma função antiga para evitar erro
DROP FUNCTION IF EXISTS reativar_cliente(UUID);

-- Cria o procedimento correto.
CREATE OR REPLACE PROCEDURE reativar_cliente(p_cliente_id UUID)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE clientes SET ativo = TRUE WHERE id = p_cliente_id;
END;
$$;