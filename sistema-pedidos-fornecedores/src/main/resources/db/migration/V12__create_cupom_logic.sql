ALTER TABLE cupons
    ADD COLUMN fornecedor_id UUID NOT NULL,
    ADD COLUMN valor_minimo_pedido DECIMAL(19, 2),
    ADD COLUMN limite_de_usos INT,
    ADD COLUMN usos_atuais INT NOT NULL DEFAULT 0;

ALTER TABLE cupons
    ADD CONSTRAINT fk_cupons_fornecedores FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id);

-- Função para cadastrar um novo cupom
CREATE OR REPLACE FUNCTION cadastrar_novo_cupom(
    p_codigo VARCHAR,
    p_tipo_desconto CHAR,
    p_valor DECIMAL,
    p_data_validade DATE,
    p_valor_minimo DECIMAL,
    p_limite_usos INT,
    p_fornecedor_id UUID
) RETURNS UUID AS $$
DECLARE
    novo_cupom_id UUID;
BEGIN
    IF EXISTS (SELECT 1 FROM cupons WHERE codigo = p_codigo) THEN
        RAISE EXCEPTION 'Código de cupom já existente: %', p_codigo
            USING ERRCODE = 'PCU01';
    END IF;

    INSERT INTO cupons (codigo, tipo_desconto, valor, data_validade, valor_minimo_pedido, limite_de_usos, usos_atuais, ativo, fornecedor_id)
    VALUES (p_codigo, p_tipo_desconto, p_valor, p_data_validade, p_valor_minimo, p_limite_usos, 0, TRUE, p_fornecedor_id)
    RETURNING id INTO novo_cupom_id;

    RETURN novo_cupom_id;
END;
$$ LANGUAGE plpgsql;


-- Função para validar o cupom de forma completa
CREATE OR REPLACE FUNCTION validar_cupom(p_codigo VARCHAR, p_valor_total_pedido DECIMAL)
RETURNS TABLE (id_cupom UUID, tipo_desconto_cupom CHAR, valor_cupom DECIMAL) AS $$
DECLARE
    cupom_encontrado cupons%ROWTYPE;
BEGIN
    SELECT * INTO cupom_encontrado FROM cupons WHERE codigo = p_codigo;

    IF NOT FOUND OR cupom_encontrado.ativo IS FALSE OR cupom_encontrado.data_validade < CURRENT_DATE THEN
        RAISE EXCEPTION 'Cupom inválido, expirado ou não encontrado.' USING ERRCODE = 'PCU02';
    END IF;

    IF cupom_encontrado.limite_de_usos IS NOT NULL AND cupom_encontrado.usos_atuais >= cupom_encontrado.limite_de_usos THEN
        RAISE EXCEPTION 'Limite de usos para o cupom % foi atingido.', p_codigo USING ERRCODE = 'PCU04';
    END IF;

    IF cupom_encontrado.valor_minimo_pedido IS NOT NULL AND p_valor_total_pedido < cupom_encontrado.valor_minimo_pedido THEN
        RAISE EXCEPTION 'O valor mínimo para usar o cupom é de R$%.', cupom_encontrado.valor_minimo_pedido USING ERRCODE = 'PCU03';
    END IF;

    RETURN QUERY SELECT cupom_encontrado.id, cupom_encontrado.tipo_desconto, cupom_encontrado.valor;
END;
$$ LANGUAGE plpgsql;


-- Procedimento para incrementar o uso de um cupom 
CREATE OR REPLACE PROCEDURE incrementar_uso_cupom(p_cupom_id UUID)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE cupons SET usos_atuais = usos_atuais + 1 WHERE id = p_cupom_id;
END;
$$;

-- Procedimento para desativar um cupom, validando a propriedade
CREATE OR REPLACE PROCEDURE desativar_cupom(p_cupom_id UUID, p_fornecedor_id_auth UUID)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cupons WHERE id = p_cupom_id AND fornecedor_id = p_fornecedor_id_auth) THEN
        RAISE EXCEPTION 'Acesso negado. O cupom não foi encontrado ou não pertence a este fornecedor.' USING ERRCODE = 'P0005';
    END IF;
    UPDATE cupons SET ativo = FALSE WHERE id = p_cupom_id;
END;
$$;

-- Procedimento para reativar um cupom, validando a propriedade
CREATE OR REPLACE PROCEDURE reativar_cupom(p_cupom_id UUID, p_fornecedor_id_auth UUID)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cupons WHERE id = p_cupom_id AND fornecedor_id = p_fornecedor_id_auth) THEN
        RAISE EXCEPTION 'Acesso negado. O cupom não foi encontrado ou não pertence a este fornecedor.' USING ERRCODE = 'P0005';
    END IF;
    UPDATE cupons SET ativo = TRUE WHERE id = p_cupom_id;
END;
$$;