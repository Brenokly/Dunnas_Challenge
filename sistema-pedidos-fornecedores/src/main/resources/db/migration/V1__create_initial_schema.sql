-- Script pra criar a estrutura inicial de tabelas para o sistema de gerenciamento de pedidos.

-- Tabela para armazenar os dados dos clientes do sistema.
CREATE TABLE clientes
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    saldo DECIMAL(19, 2) NOT NULL DEFAULT 0.00
);

-- Tabela para armazenar os dados dos fornecedores de produtos.
CREATE TABLE fornecedores
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Tabela para armazenar os produtos oferecidos pelos fornecedores.
CREATE TABLE produtos
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(19, 2) NOT NULL,
    percentual_desconto DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    fornecedor_id UUID NOT NULL,
    CONSTRAINT fk_produtos_fornecedores FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

-- Tabela para armazenar os cupons de desconto aplicáveis aos pedidos.
CREATE TABLE cupons
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(50) NOT NULL UNIQUE,
    tipo_desconto CHAR(1) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    data_validade DATE NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela principal para registrar os pedidos dos clientes.
CREATE TABLE pedidos
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data_pedido TIMESTAMP
    WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_bruto DECIMAL
    (19, 2) NOT NULL,
    valor_desconto DECIMAL
    (19, 2) NOT NULL DEFAULT 0.00,
    valor_final DECIMAL
    (19, 2) NOT NULL,
    status VARCHAR
    (50) NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
    cliente_id UUID NOT NULL,
    cupom_id UUID,
    CONSTRAINT fk_pedidos_clientes FOREIGN KEY
    (cliente_id) REFERENCES clientes
    (id),
    CONSTRAINT fk_pedidos_cupons FOREIGN KEY
    (cupom_id) REFERENCES cupons
    (id)
);

    -- Tabela de ligação para o relacionamento Muitos-para-Muitos entre Pedidos e Produtos.
    CREATE TABLE itens_pedido (
    pedido_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario_cobrado DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (pedido_id, produto_id),
    CONSTRAINT fk_itens_pedido_pedidos FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_pedido_produtos FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT
);

    -- Tabela para manter o histórico de todas as operações de pedidos e pagamentos.
    CREATE TABLE transacoes
    (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        data_transacao TIMESTAMP
        WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_transacao VARCHAR
        (100) NOT NULL,
    valor DECIMAL
        (19, 2) NOT NULL,
    cliente_id UUID NOT NULL,
    pedido_id UUID,
    CONSTRAINT fk_transacoes_clientes FOREIGN KEY
        (cliente_id) REFERENCES clientes
        (id),
    CONSTRAINT fk_transacoes_pedidos FOREIGN KEY
        (pedido_id) REFERENCES pedidos
        (id)
);