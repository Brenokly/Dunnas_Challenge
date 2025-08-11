-- Cria a tabela de categorias
CREATE TABLE categorias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE UNIQUE INDEX idx_categorias_nome_case_insensitive ON categorias (lower(nome));

-- Cria a tabela de junção entre produtos e categorias
CREATE TABLE produtos_categorias (
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    categoria_id UUID NOT NULL REFERENCES categorias(id) ON DELETE RESTRICT,
    PRIMARY KEY (produto_id, categoria_id)
);

-- Função para cadastrar uma nova categoria, com validação case-insensitive
CREATE OR REPLACE FUNCTION cadastrar_nova_categoria(p_nome VARCHAR)
RETURNS UUID AS $$
DECLARE
    nova_categoria_id UUID;
BEGIN
    INSERT INTO categorias (nome, ativo)
    VALUES (p_nome, TRUE)
    RETURNING id INTO nova_categoria_id;
    RETURN nova_categoria_id;

    EXCEPTION
        WHEN unique_violation THEN
            RAISE EXCEPTION 'Nome de categoria já existente: %', p_nome USING ERRCODE = 'PCA01';
END;
$$ LANGUAGE plpgsql;
