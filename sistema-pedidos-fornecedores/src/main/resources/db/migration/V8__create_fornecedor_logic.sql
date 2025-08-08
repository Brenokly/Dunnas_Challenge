-- Adiciona a coluna para 'soft delete' na tabela de fornecedores
ALTER TABLE fornecedores
ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;


-- Função para cadastrar um novo fornecedor com validações
CREATE OR REPLACE FUNCTION cadastrar_novo_fornecedor(
    p_nome VARCHAR, p_cnpj VARCHAR, p_usuario VARCHAR, p_senha_hash VARCHAR
) RETURNS UUID AS $$
DECLARE
    fornecedor_existente fornecedores%ROWTYPE;
    novo_fornecedor_id UUID;
BEGIN
    SELECT * INTO fornecedor_existente FROM fornecedores WHERE cnpj = p_cnpj OR usuario = p_usuario LIMIT 1;

    IF FOUND THEN
        IF fornecedor_existente.ativo IS FALSE THEN
            RAISE EXCEPTION 'Já existe uma conta de fornecedor inativa com este CNPJ ou usuário.'
                USING ERRCODE = 'PF002'; -- ERRO: CONTA FORNECEDOR INATIVA
        ELSE
            RAISE EXCEPTION 'CNPJ ou nome de usuário já cadastrado.'
                USING ERRCODE = 'PF001'; -- ERRO: DUPLICIDADE FORNECEDOR
        END IF;
    END IF;

    INSERT INTO fornecedores (nome, cnpj, usuario, senha, ativo)
    VALUES (p_nome, p_cnpj, p_usuario, p_senha_hash, TRUE)
    RETURNING id INTO novo_fornecedor_id;

    RETURN novo_fornecedor_id;
END;
$$ LANGUAGE plpgsql;


-- Procedimento para desativar um fornecedor
CREATE OR REPLACE PROCEDURE desativar_fornecedor(p_fornecedor_id UUID)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE fornecedores SET ativo = FALSE WHERE id = p_fornecedor_id;
END;
$$;


-- Procedimento para reativar um fornecedor
CREATE OR REPLACE PROCEDURE reativar_fornecedor(p_fornecedor_id UUID)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE fornecedores SET ativo = TRUE WHERE id = p_fornecedor_id;
END;
$$;