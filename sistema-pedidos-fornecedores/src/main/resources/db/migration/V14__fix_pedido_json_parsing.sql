-- Corrige a função realizar_pedido para interpretar corretamente as chaves JSON em camelCase ("produtoId")

CREATE OR REPLACE FUNCTION realizar_pedido(
    p_cliente_id UUID,
    p_itens_pedido_json JSONB,
    p_codigo_cupom VARCHAR
) RETURNS UUID AS $$
DECLARE
    item RECORD;
    produto_info RECORD;
    cupom_info RECORD;
    valor_bruto DECIMAL(19, 2) := 0;
    valor_descontos_produto DECIMAL(19, 2) := 0;
    valor_com_descontos DECIMAL(19, 2);
    valor_desconto_cupom DECIMAL(19, 2) := 0;
    valor_final DECIMAL(19, 2);
    cliente_saldo DECIMAL(19, 2);
    id_cupom_usado UUID := NULL;
    novo_pedido_id UUID;
BEGIN
    FOR item IN SELECT * FROM jsonb_to_recordset(p_itens_pedido_json) AS x("produtoId" UUID, quantidade INT)
    LOOP
        SELECT preco, percentual_desconto INTO produto_info FROM produtos WHERE id = item."produtoId" AND ativo = TRUE;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Produto com ID % não encontrado ou está inativo.', item."produtoId" USING ERRCODE = 'PPE02';
        END IF;

        valor_bruto := valor_bruto + (produto_info.preco * item.quantidade);
        valor_descontos_produto := valor_descontos_produto + (produto_info.preco * (produto_info.percentual_desconto / 100.0) * item.quantidade);
    END LOOP;

    valor_com_descontos := valor_bruto - valor_descontos_produto;

    IF p_codigo_cupom IS NOT NULL AND p_codigo_cupom <> '' THEN
        SELECT * INTO cupom_info FROM validar_cupom(p_codigo_cupom, valor_com_descontos);
        id_cupom_usado := cupom_info.id_cupom;
        IF cupom_info.tipo_desconto_cupom = 'P' THEN
            valor_desconto_cupom := valor_com_descontos * (cupom_info.valor_cupom / 100.0);
        ELSE
            valor_desconto_cupom := cupom_info.valor_cupom;
        END IF;
    END IF;

    valor_final := valor_com_descontos - valor_desconto_cupom;
    IF valor_final < 0 THEN valor_final := 0; END IF;

    SELECT saldo INTO cliente_saldo FROM clientes WHERE id = p_cliente_id FOR UPDATE;
    IF cliente_saldo < valor_final THEN
        RAISE EXCEPTION 'Saldo insuficiente para realizar o pedido.' USING ERRCODE = 'PPE01';
    END IF;

    UPDATE clientes SET saldo = saldo - valor_final WHERE id = p_cliente_id;

    INSERT INTO pedidos (cliente_id, cupom_id, valor_bruto, valor_desconto, valor_final, status)
    VALUES (p_cliente_id, id_cupom_usado, valor_bruto, (valor_descontos_produto + valor_desconto_cupom), valor_final, 'PAGO')
    RETURNING id INTO novo_pedido_id;

    INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario_cobrado)
    SELECT novo_pedido_id, (item_data->>'produtoId')::UUID, (item_data->>'quantidade')::INT,
           (SELECT preco * (1 - percentual_desconto / 100.0) FROM produtos WHERE id = (item_data->>'produtoId')::UUID)
    FROM jsonb_array_elements(p_itens_pedido_json) AS item_data;

    INSERT INTO transacoes (tipo_transacao, valor, cliente_id, pedido_id)
    VALUES ('PAGAMENTO_PEDIDO', valor_final, p_cliente_id, novo_pedido_id);

    IF id_cupom_usado IS NOT NULL THEN
        CALL incrementar_uso_cupom(id_cupom_usado);
    END IF;

    RETURN novo_pedido_id;
END;
$$ LANGUAGE plpgsql;