-- Refatora as funções de criar e atualizar produtos para gerenciar a
-- associação com a tabela de junção 'produtos_categorias' de forma atômica.

-- Função para cadastrar um novo produto e associar suas categorias.
CREATE OR REPLACE FUNCTION cadastrar_novo_produto(
    p_nome VARCHAR,
    p_descricao TEXT,
    p_preco DECIMAL,
    p_percentual_desconto DECIMAL,
    p_fornecedor_id UUID,
    p_categoria_ids UUID[]
) RETURNS UUID AS $$
DECLARE
    novo_produto_id UUID;
    categoria_id UUID;
BEGIN
    -- 1. Insere o produto na tabela principal
    INSERT INTO produtos (nome, descricao, preco, percentual_desconto, fornecedor_id, ativo)
    VALUES (p_nome, p_descricao, p_preco, p_percentual_desconto, p_fornecedor_id, TRUE)
    RETURNING id INTO novo_produto_id;

    -- 2. Itera sobre o array de IDs de categoria e insere na tabela de junção
    IF array_length(p_categoria_ids, 1) > 0 THEN
        FOREACH categoria_id IN ARRAY p_categoria_ids
        LOOP
            -- A FK constraint aqui já valida se a categoria existe.
            INSERT INTO produtos_categorias (produto_id, categoria_id)
            VALUES (novo_produto_id, categoria_id);
        END LOOP;
    END IF;

    RETURN novo_produto_id;
END;
$$ LANGUAGE plpgsql;


-- Procedimento para atualizar um produto e sincronizar suas categorias.
CREATE OR REPLACE PROCEDURE atualizar_produto(
    p_produto_id UUID,
    p_fornecedor_id_auth UUID,
    p_nome VARCHAR,
    p_descricao TEXT,
    p_preco DECIMAL,
    p_percentual_desconto DECIMAL,
    p_categoria_ids UUID[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    dono_do_produto_id UUID;
BEGIN
    -- Validação de propriedade
    SELECT fornecedor_id INTO dono_do_produto_id FROM produtos WHERE id = p_produto_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Produto não encontrado.' USING ERRCODE = 'P0004';
    END IF;
    IF dono_do_produto_id != p_fornecedor_id_auth THEN
        RAISE EXCEPTION 'Acesso negado.' USING ERRCODE = 'P0005';
    END IF;

    -- 1. Atualiza os dados básicos do produto
    UPDATE produtos SET
        nome = p_nome,
        descricao = p_descricao,
        preco = p_preco,
        percentual_desconto = p_percentual_desconto
    WHERE id = p_produto_id;

    -- 2. Sincroniza as categorias (apaga as antigas e insere as novas)
    DELETE FROM produtos_categorias WHERE produto_id = p_produto_id;
    IF array_length(p_categoria_ids, 1) > 0 THEN
        INSERT INTO produtos_categorias (produto_id, categoria_id)
        SELECT p_produto_id, unnest(p_categoria_ids);
    END IF;
END;
$$;