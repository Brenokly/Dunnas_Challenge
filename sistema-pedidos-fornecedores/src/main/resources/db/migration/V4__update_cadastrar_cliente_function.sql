-- Adiciona a verificação de contas desativadas na função de cadastro

CREATE OR REPLACE FUNCTION cadastrar_novo_cliente(
    p_nome VARCHAR,
    p_cpf VARCHAR,
    p_data_nascimento DATE,
    p_usuario VARCHAR,
    p_senha_hash VARCHAR
) RETURNS UUID AS $$
DECLARE
    cliente_existente clientes%ROWTYPE;
    novo_cliente_id UUID;
BEGIN
    -- Verifica se já existe um cliente com o mesmo CPF ou usuário
    SELECT *
    INTO cliente_existente
    FROM clientes
    WHERE cpf = p_cpf OR usuario = p_usuario
    LIMIT 1;

    IF FOUND THEN
        IF cliente_existente.ativo IS FALSE THEN
            RAISE EXCEPTION 'Regra de Negócio Violada: Já existe uma conta inativa com este CPF % ou usuário %. Contate o suporte para reativação.',
                cliente_existente.cpf, cliente_existente.usuario;
        ELSE
            RAISE EXCEPTION 'Regra de Negócio Violada: CPF % ou nome de usuário % já cadastrado.',
                cliente_existente.cpf, cliente_existente.usuario;
        END IF;
    END IF;

    -- Se não encontrou, insere o novo cliente e salva o ID na variável
    INSERT INTO clientes (
        nome, cpf, data_nascimento, usuario, senha, ativo
    ) VALUES (
        p_nome, p_cpf, p_data_nascimento, p_usuario, p_senha_hash, TRUE
    )
    RETURNING id INTO novo_cliente_id;

    -- Retorna a variável
    RETURN novo_cliente_id;
END;
$$ LANGUAGE plpgsql;