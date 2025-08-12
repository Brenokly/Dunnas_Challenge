# Guia de Testes da API (Postman)

Este documento detalha o plano de testes completo para a API de Gerenciamento de Pedidos e Pagamentos. Os testes foram projetados para serem executados em sequência para garantir a integridade dos dados e a correta transição de estados.

## 1. Configuração do Ambiente no Postman

Antes de começar, é crucial configurar o ambiente no Postman para automatizar a captura de IDs e tokens, tornando o processo de teste muito mais eficiente.

### 1.1. Criar o Workspace e a Collection

* **Workspace:** No Postman, certifique-se de que você está em um Workspace (ex: "My Workspace").
* **Collection:** Crie uma nova Collection chamada `API de Pedidos`. É aqui que todas as nossas requisições de teste ficarão organizadas.

### 1.2. Criar o Environment (Ambiente)

* No painel esquerdo, vá para a aba **Environments**.
* Clique em "Create Environment" e nomeie-o como `Desenvolvimento Local`.
* Adicione a seguinte variável inicial:

| VARIABLE | INITIAL VALUE |
| :--- | :--- |
| `BASE_URL` | `http://localhost:8080` |

* As outras variáveis serão preenchidas automaticamente pelos scripts. Deixe os valores iniciais em branco:
    * `CLIENTE_TOKEN`
    * `FORNECEDOR_TOKEN`
    * `CLIENTE_ID`
    * `FORNECEDOR_ID`
    * `PRODUTO_A_ID`
    * `PRODUTO_B_ID`
    * `CUPOM_ID`

> **Importante:** No canto superior direito do Postman, certifique-se de que o seu ambiente `Desenvolvimento Local` está selecionado para a execução dos testes.

### 1.3. Onde Colocar os Scripts

Para cada requisição listada abaixo, vá para a aba **"Tests"** e cole o código JavaScript fornecido. Este script será executado após a resposta do servidor chegar, permitindo validar o resultado e salvar variáveis para os próximos testes.

---

## FLUXO 1: CADASTRO E AUTENTICAÇÃO

### Teste 1.1: [SUCESSO] Cadastrar um novo Fornecedor
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/fornecedores`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Tech Emporium",
        "cnpj": "11222333000199",
        "usuario": "techemp",
        "senha": "password123"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", function () {
        pm.response.to.have.status(201);
    });
    const responseJson = pm.response.json();
    pm.test("Resposta contém um ID de fornecedor", function () {
        pm.expect(responseJson).to.have.property('id');
    });
    if (responseJson.id) {
        pm.environment.set("FORNECEDOR_ID", responseJson.id);
        console.log("ID do Fornecedor salvo: " + responseJson.id);
    }
    ```

### Teste 1.2: [SUCESSO] Cadastrar um novo Cliente
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/clientes`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Breno Testador",
        "cpf": "12345678901",
        "dataNascimento": "1995-01-20",
        "usuario": "brenoteste",
        "senha": "password123"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", () => pm.response.to.have.status(201));
    const responseJson = pm.response.json();
    if (responseJson.id) {
        pm.environment.set("CLIENTE_ID", responseJson.id);
    }
    ```

### Teste 1.3: [ERRO] Tentar cadastrar Fornecedor com CNPJ duplicado
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/fornecedores`
* **Body** (raw, JSON): O mesmo do Teste 1.1.
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 409 Conflict", () => pm.response.to.have.status(409));
    const responseJson = pm.response.json();
    pm.test("Mensagem de erro de duplicidade está correta", () => {
        pm.expect(responseJson.message).to.include("CNPJ ou nome de usuário já cadastrado");
    });
    ```

### Teste 1.4: [SUCESSO] Login como Fornecedor
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/auth/login`
* **Body** (raw, JSON):
    ```json
    {
        "usuario": "techemp",
        "senha": "password123"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    if (responseJson.token) {
        pm.environment.set("FORNECEDOR_TOKEN", responseJson.token);
    }
    ```

### Teste 1.5: [SUCESSO] Login como Cliente
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/auth/login`
* **Body** (raw, JSON):
    ```json
    {
        "usuario": "brenoteste",
        "senha": "password123"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    if (responseJson.token) {
        pm.environment.set("CLIENTE_TOKEN", responseJson.token);
    }
    ```

### Teste 1.6: [ERRO] Login com senha incorreta
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/auth/login`
* **Body** (raw, JSON):
    ```json
    {
        "usuario": "brenoteste",
        "senha": "senhaerrada"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 401 Unauthorized", () => pm.response.to.have.status(401));
    const responseJson = pm.response.json();
    pm.test("Mensagem de erro está correta", () => {
        pm.expect(responseJson.message).to.equal("Usuário ou senha inválidos.");
    });
    ```

---

## FLUXO 2: GERENCIAMENTO DE PRODUTOS E CUPONS (FORNECEDOR)

**Requisito:** Todas as requisições deste fluxo devem usar o `{{FORNECEDOR_TOKEN}}` na aba `Authorization`, selecionando `Bearer Token`.

### Teste 2.1: [SUCESSO] Fornecedor cadastra um novo Produto
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/produtos`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Mouse Gamer UltraLight",
        "descricao": "Mouse ergonômico com 12000 DPI.",
        "preco": 250.00,
        "percentualDesconto": 10,
        "categoriaIds": []
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", () => pm.response.to.have.status(201));
    const responseJson = pm.response.json();
    if (responseJson.id) {
        pm.environment.set("PRODUTO_A_ID", responseJson.id);
    }
    ```

### Teste 2.2: [SUCESSO] Fornecedor cadastra um segundo Produto
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/produtos`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Headset Pro 7.1",
        "descricao": "Headset com som surround virtual.",
        "preco": 450.00,
        "percentualDesconto": 0,
        "categoriaIds": []
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", () => pm.response.to.have.status(201));
    const responseJson = pm.response.json();
    if (responseJson.id) {
        pm.environment.set("PRODUTO_B_ID", responseJson.id);
    }
    ```

### Teste 2.3: [SUCESSO] Fornecedor cadastra um Cupom
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/cupons`
* **Body** (raw, JSON):
    ```json
    {
        "codigo": "PRIMEIRACOMPRA",
        "tipoDesconto": "P",
        "valor": 15,
        "dataValidade": "2025-12-31",
        "valorMinimoPedido": 200.00,
        "limiteDeUsos": 100
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", () => pm.response.to.have.status(201));
    const responseJson = pm.response.json();
    if (responseJson.id) {
        pm.environment.set("CUPOM_ID", responseJson.id);
    }
    ```

### Teste 2.4: [SUCESSO] Fornecedor lista seus próprios produtos
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/produtos/meus-produtos`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Lista de produtos contém 2 itens", () => {
        pm.expect(responseJson.content).to.have.lengthOf(2);
    });
    ```

### Teste 2.5: [SUCESSO] Fornecedor lista seus próprios cupons
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/cupons`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Lista de cupons contém 1 item", () => {
        pm.expect(responseJson.content).to.have.lengthOf(1);
    });
    ```

### Teste 2.6: [ERRO] Cliente tenta cadastrar um produto
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/produtos`
* **Header:** `Authorization: Bearer {{CLIENTE_TOKEN}}`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Produto Ilegal",
        "descricao": "Produto de teste que um cliente não pode criar.",
        "preco": 10.00,
        "percentualDesconto": 0,
        "categoriaIds": []
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 403 Forbidden", () => pm.response.to.have.status(403));
    ```

---

## FLUXO 3: JORNADA DO CLIENTE E REALIZAÇÃO DE PEDIDOS

**Requisito:** Todas as requisições deste fluxo devem usar o `{{CLIENTE_TOKEN}}` na aba `Authorization`, selecionando `Bearer Token`.

### Teste 3.1: [SUCESSO] Cliente atualiza seus dados
* **Método:** `PATCH`
* **URL:** `{{BASE_URL}}/api/v1/clientes/{{CLIENTE_ID}}/dados-pessoais`
* **Body** (raw, JSON):
    ```json
    {
        "nome": "Breno Atualizado",
        "dataNascimento": "1995-01-20"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Nome foi atualizado", () => {
        pm.expect(responseJson.nome).to.equal("Breno Atualizado");
    });
    ```

### Teste 3.2: [SUCESSO] Cliente adiciona saldo à sua conta
* **Método:** `PATCH`
* **URL:** `{{BASE_URL}}/api/v1/clientes/meu-perfil/saldo`
* **Body** (raw, JSON):
    ```json
    {
        "valor": 2000.00
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Saldo foi atualizado para 2000.00", () => {
        pm.expect(responseJson.saldo).to.equal(2000.00);
    });
    ```

### Teste 3.3: [SUCESSO] Cliente lista produtos (visão pública)
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/produtos`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Lista de produtos contém 2 itens", () => {
        pm.expect(responseJson.content).to.have.lengthOf(2);
    });
    ```

### Teste 3.4: [SUCESSO] Cliente realiza um Pedido
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/pedidos`
* **Body** (raw, JSON):
    ```json
    {
        "itens": [
            {
                "produtoId": "{{PRODUTO_A_ID}}",
                "quantidade": 1
            },
            {
                "produtoId": "{{PRODUTO_B_ID}}",
                "quantidade": 2
            }
        ],
        "codigoCupom": "PRIMEIRACOMPRA"
    }
    ```
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 201 Created", () => pm.response.to.have.status(201));
    const responseJson = pm.response.json();
    pm.test("Cálculos do pedido estão corretos", () => {
        pm.expect(responseJson.valorBruto).to.equal(1150.00);
        pm.expect(responseJson.valorFinal).to.equal(956.25);
    });
    ```

### Teste 3.5: [ERRO] Cliente tenta realizar pedido com saldo insuficiente
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/pedidos`
* **Body** (raw, JSON): O mesmo do Teste 3.4.
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 400 Bad Request", () => pm.response.to.have.status(400));
    const responseJson = pm.response.json();
    pm.test("Mensagem de saldo insuficiente está correta", () => {
        pm.expect(responseJson.message).to.include("Saldo insuficiente");
    });
    ```

### Teste 3.6: [SUCESSO] Cliente lista seu histórico de pedidos
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/pedidos`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Histórico de pedidos não está vazio", () => {
        pm.expect(responseJson.content).to.not.be.empty;
    });
    ```

### Teste 3.7: [SUCESSO] Cliente lista seu histórico de transações
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/transacoes/meu-historico`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    pm.test("Histórico contém transação de saldo e de pedido", () => {
        const tipos = responseJson.content.map(t => t.tipoTransacao);
        pm.expect(tipos).to.include("ADICAO_SALDO");
        pm.expect(tipos).to.include("PAGAMENTO_PEDIDO");
    });
    ```

---

## FLUXO 4: DESATIVAÇÃO E REATIVAÇÃO

### Teste 4.1: [SUCESSO] Fornecedor desativa sua conta
* **Método:** `DELETE`
* **URL:** `{{BASE_URL}}/api/v1/fornecedores/{{FORNECEDOR_ID}}`
* **Header:** `Authorization: Bearer {{FORNECEDOR_TOKEN}}`
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 204 No Content", () => pm.response.to.have.status(204));
    ```

### Teste 4.2: [ERRO] Tentar fazer login com fornecedor desativado
* **Método:** `POST`
* **URL:** `{{BASE_URL}}/api/v1/auth/login`
* **Body** (raw, JSON): Credenciais do fornecedor (`usuario`: "techemp", `senha`: "password123").
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 401 Unauthorized", () => pm.response.to.have.status(401));
    ```

### Teste 4.3: [SUCESSO] Produtos do fornecedor desativado não aparecem na listagem pública
* **Método:** `GET`
* **URL:** `{{BASE_URL}}/api/v1/produtos`
* **Header:** Nenhuma autenticação.
* **Script** (aba "Tests"):
    ```javascript
    pm.test("Status da Resposta é 200 OK", () => pm.response.to.have.status(200));
    const responseJson = pm.response.json();
    const produtoA_Id = pm.environment.get("PRODUTO_A_ID");

    const produtoEncontrado = responseJson.content.find(p => p.id === produtoA_Id);
    pm.test("Produto do fornecedor desativado não está na lista pública", () => {
        pm.expect(produtoEncontrado).to.be.undefined;
    });
    ```