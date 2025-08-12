CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE PROCEDURE atualizar_cliente(
    p_cliente_id UUID,
    p_novo_nome VARCHAR,
    p_nova_data_nascimento DATE,
    p_senha_atual_texto VARCHAR,
    p_nova_senha_hash VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    hash_senha_salva VARCHAR;
BEGIN
    IF p_nova_senha_hash IS NOT NULL AND p_nova_senha_hash <> '' THEN
        IF p_senha_atual_texto IS NULL OR p_senha_atual_texto = '' THEN
            RAISE EXCEPTION 'A senha atual é necessária para definir uma nova.' USING ERRCODE = 'P0007';
        END IF;
        SELECT senha INTO hash_senha_salva FROM clientes WHERE id = p_cliente_id;
        IF hash_senha_salva IS NULL OR hash_senha_salva != crypt(p_senha_atual_texto, hash_senha_salva) THEN
            RAISE EXCEPTION 'A senha atual está incorreta.' USING ERRCODE = 'P0006';
        END IF;
    END IF;

    UPDATE clientes
    SET
        nome = COALESCE(p_novo_nome, nome),
        data_nascimento = COALESCE(p_nova_data_nascimento, data_nascimento),
        senha = COALESCE(p_nova_senha_hash, senha)
    WHERE
        id = p_cliente_id;
END;
$$;