-- Procedimento para atualizar os dados de um cliente.

CREATE OR REPLACE PROCEDURE atualizar_cliente(
    p_cliente_id UUID,
    p_novo_nome VARCHAR,
    p_nova_data_nascimento DATE,
    p_novo_senha_hash VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE clientes
    SET
        nome = p_novo_nome,
        data_nascimento = p_nova_data_nascimento,

        senha = COALESCE(p_novo_senha_hash, senha)
    WHERE
        id = p_cliente_id;
END;
$$;