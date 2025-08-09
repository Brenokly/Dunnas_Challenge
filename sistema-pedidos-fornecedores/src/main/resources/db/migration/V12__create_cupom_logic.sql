-- Função para cadastrar um novo cupom, validando a unicidade do código.
CREATE OR REPLACE FUNCTION cadastrar_novo_cupom(
    p_codigo VARCHAR,
    p_tipo_desconto CHAR,
    p_valor DECIMAL,
    p_data_validade DATE
) RETURNS UUID AS $$
DECLARE
    novo_cupom_id UUID;
BEGIN
    -- Validação: Verifica se o código do cupom já existe
    IF EXISTS (SELECT 1 FROM cupons WHERE codigo = p_codigo) THEN
        RAISE EXCEPTION 'Código de cupom já existente: %', p_codigo
            USING ERRCODE = 'PCU01';
    END IF;

    INSERT INTO cupons (codigo, tipo_desconto, valor, data_validade, ativo)
    VALUES (p_codigo, p_tipo_desconto, p_valor, p_data_validade, TRUE)
    RETURNING id INTO novo_cupom_id;

    RETURN novo_cupom_id;
END;
$$ LANGUAGE plpgsql;


-- Função para validar um cupom no ato da compra.
CREATE OR REPLACE FUNCTION validar_cupom(p_codigo VARCHAR)
RETURNS TABLE (
    id_cupom UUID,
    tipo_desconto_cupom CHAR,
    valor_cupom DECIMAL
) AS $$
DECLARE
    cupom_encontrado cupons%ROWTYPE;
BEGIN
    SELECT * INTO cupom_encontrado FROM cupons WHERE codigo = p_codigo;

    IF NOT FOUND OR cupom_encontrado.ativo IS FALSE OR cupom_encontrado.data_validade < CURRENT_DATE THEN
        RAISE EXCEPTION 'Cupom inválido, expirado ou não encontrado: %', p_codigo
            USING ERRCODE = 'PCU02';
    END IF;

    -- Se for válido, retorna os dados necessários para o cálculo do desconto
    RETURN QUERY SELECT cupom_encontrado.id, cupom_encontrado.tipo_desconto, cupom_encontrado.valor;
END;
$$ LANGUAGE plpgsql;