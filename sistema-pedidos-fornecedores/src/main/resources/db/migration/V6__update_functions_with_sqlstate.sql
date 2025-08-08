-- V6__update_functions_with_sqlstate.sql

-- Atualiza as funções já criadas para usar o SQLSTATEs customizados

CREATE OR REPLACE FUNCTION cadastrar_novo_cliente(
    p_nome VARCHAR, p_cpf VARCHAR, p_data_nascimento DATE, p_usuario VARCHAR, p_senha_hash VARCHAR
) RETURNS UUID AS $$
DECLARE
    cliente_existente clientes%ROWTYPE;
    novo_cliente_id UUID;
BEGIN
    SELECT * INTO cliente_existente FROM clientes WHERE cpf = p_cpf OR usuario = p_usuario LIMIT 1;

    IF FOUND THEN
        IF cliente_existente.ativo IS FALSE THEN
            RAISE EXCEPTION 'Já existe uma conta inativa com este CPF ou usuário. Contate o suporte para reativação.'
                USING ERRCODE = 'PC002';
        ELSE
            RAISE EXCEPTION 'CPF ou nome de usuário já cadastrado.'
                USING ERRCODE = 'PC001'; 
        END IF;
    END IF;

    INSERT INTO clientes (nome, cpf, data_nascimento, usuario, senha, ativo)
    VALUES (p_nome, p_cpf, p_data_nascimento, p_usuario, p_senha_hash, TRUE)
    RETURNING id INTO novo_cliente_id;

    RETURN novo_cliente_id;
END;
$$ LANGUAGE plpgsql;


-- Atualiza a função de adicionar saldo para usar SQLSTATEs customizados
CREATE OR REPLACE FUNCTION adicionar_saldo_cliente(
    p_cliente_id UUID,
    p_valor_adicao DECIMAL
)
RETURNS DECIMAL AS $$
DECLARE
    novo_saldo DECIMAL;
BEGIN
    IF p_valor_adicao <= 0 THEN
        RAISE EXCEPTION 'O valor a ser adicionado deve ser positivo.'
            USING ERRCODE = 'PC003';
    END IF;

    UPDATE clientes SET saldo = saldo + p_valor_adicao WHERE id = p_cliente_id
    RETURNING saldo INTO novo_saldo;

    INSERT INTO transacoes(data_transacao, tipo_transacao, valor, cliente_id, pedido_id)
    VALUES(CURRENT_TIMESTAMP, 'ADICAO_SALDO', p_valor_adicao, p_cliente_id, NULL);

    RETURN novo_saldo;
END;
$$ LANGUAGE plpgsql;