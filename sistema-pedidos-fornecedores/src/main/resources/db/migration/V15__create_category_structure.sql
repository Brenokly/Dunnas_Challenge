CREATE TABLE
  categorias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
    nome VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
  );

CREATE TABLE
  produtos_categorias (
    produto_id UUID NOT NULL REFERENCES produtos (id) ON DELETE CASCADE,
    categoria_id UUID NOT NULL REFERENCES categorias (id) ON DELETE RESTRICT,
    PRIMARY KEY (produto_id, categoria_id)
  );