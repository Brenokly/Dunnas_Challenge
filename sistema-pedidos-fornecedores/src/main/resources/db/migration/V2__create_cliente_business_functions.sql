-- Função para cadastrar um novo cliente, com validações de duplicidade.
CREATE OR REPLACE PROCEDURE cadastrar_novo_cliente
(
    p_nome VARCHAR, p_cpf VARCHAR, p_data_nascimento DATE, p_usuario VARCHAR, p_senha_hash VARCHAR
) RETURNS UUID AS $$
LANGUAGE plpgsql
DECLARE
    novo_cliente_id UUID;
BEGIN
    IF EXISTS (SELECT 1
    FROM clientes
    WHERE cpf = p_cpf) THEN
        RAISE EXCEPTION 'Regra de Negócio Violada: CPF já cadastrado (%)', p_cpf;
END
IF;

    IF EXISTS (SELECT 1
FROM clientes
WHERE usuario = p_usuario) THEN
        RAISE EXCEPTION 'Regra de Negócio Violada: Nome de usuário já existe (%)', p_usuario;
END
IF;

    INSERT INTO clientes
    (nome, cpf, data_nascimento, usuario, senha, saldo)
VALUES
    (p_nome, p_cpf, p_data_nascimento, p_usuario, p_senha_hash, 0.00)
RETURNING id INTO novo_cliente_id;

RETURN novo_cliente_id;
END;
$$ LANGUAGE plpgsql;


-- Função para adicionar saldo a um cliente e registrar a transação de forma atômica.
CREATE OR REPLACE PROCEDURE adicionar_saldo_cliente
(
    p_cliente_id UUID,
    p_valor_adicao DECIMAL
)
RETURNS DECIMAL AS $$
DECLARE
    novo_saldo DECIMAL;
BEGIN
    -- Validação: O valor da adição não pode ser negativo ou zero.
    IF p_valor_adicao <= 0 THEN
        RAISE EXCEPTION 'Regra de Negócio Violada: O valor a ser adicionado deve ser positivo.';
END
IF;

    -- Transação: Atualiza o saldo do cliente.
    UPDATE clientes
    SET saldo = saldo + p_valor_adicao
    WHERE id = p_cliente_id
RETURNING saldo INTO novo_saldo;

-- Transação: Registra a operação no histórico.
INSERT INTO transacoes
    (data_transacao, tipo_transacao, valor, cliente_id, pedido_id)
VALUES(CURRENT_TIMESTAMP, 'ADICAO_SALDO', p_valor_adicao, p_cliente_id, NULL);

RETURN novo_saldo;
END;
$$ LANGUAGE plpgsql;