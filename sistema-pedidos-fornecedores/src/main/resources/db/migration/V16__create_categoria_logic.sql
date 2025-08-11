-- Procedimento para atualizar o nome de uma categoria.
CREATE OR REPLACE PROCEDURE atualizar_categoria(
    p_categoria_id UUID,
    p_novo_nome VARCHAR
) LANGUAGE plpgsql AS $$
BEGIN
    -- Validação de duplicidade para o novo nome
    IF EXISTS (SELECT 1 FROM categorias WHERE lower(nome) = lower(p_novo_nome) AND id != p_categoria_id) THEN
        RAISE EXCEPTION 'Nome de categoria já existente: %', p_novo_nome
            USING ERRCODE = 'PCA01';
    END IF;

    UPDATE categorias SET nome = p_novo_nome WHERE id = p_categoria_id;
END;
$$;


-- Procedimento para desativar uma categoria (soft delete).
CREATE OR REPLACE PROCEDURE desativar_categoria(p_categoria_id UUID)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE categorias SET ativo = FALSE WHERE id = p_categoria_id;
END;
$$;


-- Procedimento para reativar uma categoria.
CREATE OR REPLACE PROCEDURE reativar_categoria(p_categoria_id UUID)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE categorias SET ativo = TRUE WHERE id = p_categoria_id;
END;
$$;