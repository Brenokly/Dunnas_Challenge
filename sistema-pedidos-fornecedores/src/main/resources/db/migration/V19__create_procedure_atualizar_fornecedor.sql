CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE PROCEDURE atualizar_senha_fornecedor(
    p_fornecedor_id UUID,
    p_senha_atual_texto VARCHAR,
    p_nova_senha_hash VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    hash_senha_salva VARCHAR;
BEGIN
    -- Verifica se a nova senha foi fornecida
    IF p_nova_senha_hash IS NULL OR p_nova_senha_hash = '' THEN
        RAISE EXCEPTION 'A nova senha é obrigatória.' USING ERRCODE = 'P0008';
    END IF;

    -- Verifica se a senha atual foi fornecida
    IF p_senha_atual_texto IS NULL OR p_senha_atual_texto = '' THEN
        RAISE EXCEPTION 'A senha atual é obrigatória para alteração.' USING ERRCODE = 'P0007';
    END IF;

    -- Busca senha salva no banco
    SELECT senha INTO hash_senha_salva
    FROM fornecedores
    WHERE id = p_fornecedor_id;

    -- Valida se a senha atual está correta
    IF hash_senha_salva IS NULL OR hash_senha_salva != crypt(p_senha_atual_texto, hash_senha_salva) THEN
        RAISE EXCEPTION 'A senha atual está incorreta.' USING ERRCODE = 'P0006';
    END IF;

    -- Atualiza somente a senha
    UPDATE fornecedores
    SET senha = p_nova_senha_hash
    WHERE id = p_fornecedor_id;
END;
$$;