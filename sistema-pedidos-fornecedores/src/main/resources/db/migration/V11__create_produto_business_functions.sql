-- Função para cadastrar um novo produto, garantindo a associação a um fornecedor.
CREATE OR REPLACE FUNCTION cadastrar_novo_produto(
    p_nome VARCHAR,
    p_descricao TEXT,
    p_preco DECIMAL,
    p_percentual_desconto DECIMAL,
    p_fornecedor_id UUID
) RETURNS UUID AS $$
DECLARE
    novo_produto_id UUID;
BEGIN
    INSERT INTO produtos (nome, descricao, preco, percentual_desconto, fornecedor_id, ativo)
    VALUES (p_nome, p_descricao, p_preco, p_percentual_desconto, p_fornecedor_id, TRUE)
    RETURNING id INTO novo_produto_id;

    RETURN novo_produto_id;
END;
$$ LANGUAGE plpgsql;


-- Procedimento para atualizar um produto, validando se o fornecedor é o dono.
CREATE OR REPLACE PROCEDURE atualizar_produto(
    p_produto_id UUID,
    p_fornecedor_id_auth UUID,
    p_nome VARCHAR,
    p_descricao TEXT,
    p_preco DECIMAL,
    p_percentual_desconto DECIMAL
)
LANGUAGE plpgsql
AS $$
DECLARE
    dono_do_produto_id UUID;
BEGIN
    -- Validação de propriedade: Verifica se o produto pertence ao fornecedor que tenta editá-lo.
    SELECT fornecedor_id INTO dono_do_produto_id FROM produtos WHERE id = p_produto_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Produto não encontrado.' USING ERRCODE = 'P0004'; -- ERRO: PRODUTO NAO ENCONTRADO
    END IF;

    IF dono_do_produto_id != p_fornecedor_id_auth THEN
        RAISE EXCEPTION 'Acesso negado. O fornecedor não é o proprietário deste produto.'
            USING ERRCODE = 'P0005'; -- ERRO: ACESSO NEGADO
    END IF;

    -- Se a validação passar, atualiza o produto.
    UPDATE produtos SET
        nome = p_nome,
        descricao = p_descricao,
        preco = p_preco,
        percentual_desconto = p_percentual_desconto
    WHERE id = p_produto_id;
END;
$$;


-- Procedimento para desativar (soft delete) um produto, com validação de propriedade.
CREATE OR REPLACE PROCEDURE desativar_produto(
    p_produto_id UUID,
    p_fornecedor_id_auth UUID
)
LANGUAGE plpgsql
AS $$
DECLARE
    dono_do_produto_id UUID;
BEGIN
    -- VALIDAÇÃO DE PROPRIEDADE
    SELECT fornecedor_id INTO dono_do_produto_id FROM produtos WHERE id = p_produto_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Produto não encontrado.' USING ERRCODE = 'P0004';
    END IF;

    IF dono_do_produto_id != p_fornecedor_id_auth THEN
        RAISE EXCEPTION 'Acesso negado. O fornecedor não é o proprietário deste produto.'
            USING ERRCODE = 'P0005';
    END IF;

    UPDATE produtos SET ativo = FALSE WHERE id = p_produto_id;
END;
$$;


-- Procedimento para reativar um produto, com validação de propriedade.
CREATE OR REPLACE PROCEDURE reativar_produto(
    p_produto_id UUID,
    p_fornecedor_id_auth UUID
)
LANGUAGE plpgsql
AS $$
DECLARE
    dono_do_produto_id UUID;
BEGIN
    -- VALIDAÇÃO DE PROPRIEDADE
    SELECT fornecedor_id INTO dono_do_produto_id FROM produtos WHERE id = p_produto_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Produto não encontrado.' USING ERRCODE = 'P0004';
    END IF;

    IF dono_do_produto_id != p_fornecedor_id_auth THEN
        RAISE EXCEPTION 'Acesso negado. O fornecedor não é o proprietário deste produto.'
            USING ERRCODE = 'P0005';
    END IF;

    UPDATE produtos SET ativo = TRUE WHERE id = p_produto_id;
END;
$$;