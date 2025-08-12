# Sistema de Gerenciamento de Pedidos e Pagamentos de Fornecedores

Este projeto é um Sistema de Gerenciamento de Pedidos e Pagamentos de Fornecedores, desenvolvido como parte do Desafio Fullstack Java da Dunnas Tecnologia.

O objetivo principal foi construir uma API RESTful robusta, segura e escalável, seguindo rigorosamente os requisitos do desafio e aplicando as melhores práticas e princípios de design de software do mercado.

## 📜 Requisitos do Desafio

O sistema permite que clientes façam pedidos a fornecedores com base nos produtos que estes oferecem. O produto pode ter o valor cheio, estar com desconto ou o consumidor pode inserir um cupom de desconto no ato da compra. O sistema deve permitir o cadastro, autenticação, realização de pedidos, pagamentos e visualização do histórico de transações.

- **Cliente:** Deve ter nome, CPF, data de nascimento, usuário, senha e saldo.
- **Fornecedor:** Deve ter nome, CNPJ, usuário, senha e os produtos que eles oferecem.
- **Produto:** Deve ter nome, descrição e preço.
- **Histórico:** O sistema deve manter o histórico de todas as operações de pedidos e pagamentos.

### Stack Tecnológica
* **Backend:** Java com Spring Boot
* **View:** Java Server Pages (JSP)
* **Banco de dados:** PostgreSQL

### Versionamento de Banco de Dados
* Utilização do **Flyway** para versionar todas as alterações no banco, garantindo que o banco de dados possa ser recriado do zero.

### Lógica de Negócio
* Pelo menos **50% da lógica de negócio** (transações, validações, cálculos) deve ser implementada diretamente no banco de dados.

---

## ✨ Funcionalidades Implementadas

O backend está 100% funcional e oferece um conjunto completo de operações para os três principais atores do sistema: Clientes, Fornecedores e a Plataforma.

### Para Clientes
* **Cadastro e Autenticação:** Um novo cliente pode se cadastrar e fazer login para obter um token de acesso JWT.
* **Gestão de Perfil:** Um cliente autenticado pode buscar os dados do seu próprio perfil e desativar sua conta (soft delete).
* **Gestão Financeira:** Um cliente pode adicionar saldo à sua conta e visualizar seu histórico completo de transações, com a opção de filtrar por tipo.
* **Jornada de Compra:**
    * Visualizar a "vitrine" de produtos ativos, podendo ser filtrada por categoria.
    * Listar todos os produtos disponíveis para compra.
    * Realizar Pedidos: Criar um pedido com múltiplos produtos, aplicar cupons de desconto e pagar utilizando o saldo da conta.
* **Histórico:** Visualizar o histórico completo de seus próprios pedidos, com a opção de filtrar por status (ex: 'PAGO', 'ENVIADO').

### Para Fornecedores
* **Cadastro e Autenticação:** Um novo fornecedor pode se cadastrar e fazer login para obter um token de acesso JWT.
* **Gestão de Perfil:** Um fornecedor autenticado pode buscar os dados do seu próprio perfil e desativar sua conta.
* **Gestão de Produtos:**
    * Cadastrar novos produtos.
    * Atualizar os dados de seus produtos.
    * Listar todos os seus produtos (incluindo ativos e inativos).
    * Desativar (soft delete) e reativar seus produtos.
* **Gestão de Cupons:**
    * Criar cupons de desconto flexíveis (percentual ou valor fixo), com data de validade e outras regras.
    * Listar, desativar e reativar seus próprios cupons.

---

## 🏗️ Arquitetura e Princípios de Design

A arquitetura do projeto foi o pilar para garantir a qualidade, manutenibilidade e escalabilidade do código.

### Clean Architecture (Abordagem Pragmática)
O projeto foi estruturado seguindo os princípios da Clean Architecture, com uma separação clara em camadas:

![CleanArchitecture](https://github.com/user-attachments/assets/614b58de-9a70-4498-84c6-b384450c3774)

* **`domain`**: O núcleo do sistema, contendo as entidades de domínio (JPA), e Enums que representam estados de negócio.
* **`application`**: Define os Casos de Uso do sistema através de interfaces (`ClienteUseCases`, `ProdutoUseCases`), que servem como um contrato claro para a lógica de negócio.
* **`infrastructure`**: A camada externa, contendo os detalhes de implementação: `Controllers` da API, `Repositories` para acesso ao banco, `Services` que implementam os casos de uso, e a configuração de segurança.

### Aderência aos Princípios SOLID
* **(S) Responsabilidade Única:** Cada classe tem um propósito. O `ClienteController` lida com HTTP, o `ClienteServiceImpl` orquestra o caso de uso, e o `ClienteRepository` acessa os dados.
* **(O) Aberto/Fechado:** Componentes abertos para extensão, mas fechados para modificação. O sistema de tratamento de erros com o `SqlStateErrorMapping` permite adicionar novos erros sem modificar a lógica existente.
* **(L) Substituição de Liskov:** Os controllers dependem de interfaces (`ClienteUseCases`), não de implementações concretas, permitindo a substituição de implementações sem quebrar o sistema.
* **(I) Segregação de Interfaces:** Interfaces coesas como `ClienteUseCases` e `ProdutoUseCases` em vez de uma única interface genérica.
* **(D) Inversão de Dependência:** Uso de injeção de dependência via construtor, sempre injetando abstrações (interfaces).

---

## 🛠️ Decisões Técnicas Chave

### Lógica de Negócio no Banco de Dados (Requisito Crítico)
Para cumprir a regra de 50% da lógica no banco, implementamos:
* **Validações:** Verificação de duplicidade (CPF, CNPJ, etc.) e de propriedade via `FUNCTION`s e `PROCEDURE`s.
* **Cálculos:** A lógica complexa de cálculo do valor final de um pedido está encapsulada na função `realizar_pedido`.
* **Transações Atômicas:** A operação `realizar_pedido` é uma única transação que garante a consistência dos dados.

### Versionamento com Flyway
O diretório `db/migration` contém o histórico imutável da evolução do banco de dados, garantindo a recriação e atualização segura em qualquer ambiente.

### Segurança da API (Spring Security & JWT)
A API é protegida por um sistema de autenticação e autorização robusto e stateless, baseado em JSON Web Tokens (JWT).
* **Autenticação Unificada:** Um único endpoint (`/api/v1/auth/login`) para `Clientes` e `Fornecedores`.
* **Segurança de Endpoints:** Rotas protegidas por padrão, com liberação explícita para endpoints públicos.
* **Tratamento de Erros Profissional:** Sistema centralizado usando AOP para traduzir erros do banco em exceções de negócio e respostas HTTP padronizadas.

### Padrões de Projeto Aplicados
* **DTO (Data Transfer Object)**
* **Mapper**
* **Strategy / Factory**
* **AOP (Programação Orientada a Aspectos)**

---

## 📁 Estrutura de Pastas do Projeto
```
src
└── main
├── java
│   └── br/com/dunnastecnologia/sistemapedidosfornecedores/
│       ├── application
│       │   └── usecases/         # Interfaces (Contratos) dos Casos de Uso
│       ├── domain
│       │   ├── model/            # Entidades de Domínio (JPA)
│       │   └── utils/
│       │       └── enums/        # Enums de negócio
│       └── infrastructure
│           ├── config/           # Configurações do Spring e Aspectos
│           ├── controller/
│           │   ├── api/          # Controladores da API REST
│           │   └── web/          # Controladores da View/Páginas
│           ├── dto/              # Data Transfer Objects
│           ├── exception/        # Exceções customizadas
│           ├── mapper/           # Mapeadores de Entidade para DTO
│           ├── repository/       # Interfaces Spring Data JPA
│           ├── security/         # Componentes de segurança (JWT)
│           └── service/          # Implementações dos Casos de Uso
└── resources
├── db
│   └── migration/            # Scripts de migração do Flyway
├── static
│   ├── css/
│   ├── images/
│   └── js/
└── application.properties    # Arquivo de configuração principal
└── webapp
└── WEB-INF
└── jsp/
├── includes/             # Componentes JSP reutilizáveis
└── ...                   # Demais páginas .jsp
```
---

## 🔌 Documentação da API (Endpoints)

Lista completa de todos os endpoints da API.

### Autenticação (`/api/v1/auth`)
* `POST /login`: Autentica um usuário (Cliente ou Fornecedor) e retorna um token JWT. (Público)

### Clientes (`/api/v1/clientes`)
* `POST /`: Cadastra um novo cliente. (Público)
* `GET /meu-perfil`: Busca os dados do cliente autenticado. (Autenticado - Cliente)
* `PATCH /{id}/dados-pessoais`: Atualiza nome e/ou data de nascimento. (Autenticado - Cliente)
* `PATCH /{id}/senha`: Atualiza a senha do cliente. (Autenticado - Cliente)
* `PATCH /meu-perfil/saldo`: Adiciona saldo à conta. (Autenticado - Cliente)
* `DELETE /{id}`: Desativa a conta de um cliente. (Autenticado - Cliente)

### Fornecedores (`/api/v1/fornecedores`)
* `POST /`: Cadastra um novo fornecedor. (Público)
* `GET /`: Lista todos os fornecedores ativos. (Público)
* `GET /{id}`: Busca um fornecedor ativo por ID. (Público)
* `GET /{fornecedorId}/produtos`: Lista os produtos ativos de um fornecedor. (Público)
* `GET /meu-perfil`: Busca os dados do fornecedor autenticado. (Autenticado - Fornecedor)
* `PATCH /{id}/senha`: Atualiza a senha de um fornecedor. (Autenticado - Fornecedor)
* `DELETE /{id}`: Desativa a conta de um fornecedor. (Autenticado - Fornecedor)

### Produtos (`/api/v1/produtos`)
* `GET /`: Lista todos os produtos ativos. (Público)
* `GET /{id}`: Busca um produto ativo por ID. (Público)
* `POST /`: Fornecedor cadastra um novo produto. (Autenticado - Fornecedor)
* `GET /meus-produtos`: Fornecedor lista todos os seus produtos. (Autenticado - Fornecedor)
* `PUT /{id}`: Fornecedor atualiza um de seus produtos. (Autenticado - Fornecedor)
* `DELETE /{id}`: Fornecedor desativa um de seus produtos. (Autenticado - Fornecedor)
* `POST /{id}/reativar`: Fornecedor reativa um de seus produtos. (Autenticado - Fornecedor)

### Cupons (`/api/v1/cupons`)
* `POST /`: Fornecedor cria um novo cupom. (Autenticado - Fornecedor)
* `GET /{fornecedorId}/cupom/{codigo}`: Valida um cupom. (Autenticado - Cliente/Fornecedor)
* `GET /`: Fornecedor lista seus próprios cupons. (Autenticado - Fornecedor)
* `GET /{id}`: Fornecedor busca um de seus cupons por ID. (Autenticado - Fornecedor)
* `DELETE /{id}`: Fornecedor desativa um de seus cupons. (Autenticado - Fornecedor)
* `POST /{id}/reativar`: Fornecedor reativa um de seus cupons. (Autenticado - Fornecedor)

### Pedidos (`/api/v1/pedidos`)
* `POST /`: Cliente cria um novo pedido. (Autenticado - Cliente)
* `GET /`: Cliente lista seu histórico de pedidos. (Autenticado - Cliente)
* `GET /{id}`: Cliente busca um de seus pedidos por ID. (Autenticado - Cliente)
* `GET /fornecedor`: Fornecedor lista seus pedidos. (Autenticado - Fornecedor)
* `GET /fornecedor/{id}`: Fornecedor busca os detalhes de um de seus pedidos. (Autenticado - Fornecedor)

### Transações (`/api/v1/transacoes`)
* `GET /meu-historico`: Cliente lista seu histórico de transações. (Autenticado - Cliente)

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java (JDK):** Versão 21 ou superior.
* **Apache Maven:** Versão 3.8 ou superior.
* **Git**
* **PostgreSQL:** Versão 15 ou superior.
* **IDE (Opcional):** VS Code, IntelliJ IDEA ou Eclipse.

### Guia de Instalação e Execução

1.  **Clonar o Repositório**
    ```bash
    git clone [https://github.com/Brenokly/Dunnas_Challenge.git](https://github.com/Brenokly/Dunnas_Challenge.git)
    cd Dunnas_Challenge
    ```

2.  **Configurar o Banco de Dados PostgreSQL**
    * Crie um novo banco de dados. O nome padrão esperado é `dunnas_challenge_db`.
    ```sql
    CREATE DATABASE dunnas_challenge_db;
    ```

3.  **Configurar a Aplicação**
    * Navegue até o arquivo: `src/main/resources/application.properties`.
    * Edite as credenciais do banco de dados para que correspondam à sua configuração local.
    ```properties
    # CONFIGURAÇÃO DO BANCO DE DADOS (POSTGRESQL)
    spring.datasource.url=jdbc:postgresql://localhost:5432/dunnas_challenge_db
    spring.datasource.username=seu_usuario_aqui
    spring.datasource.password=sua_senha_aqui
    ```

4.  **Compilar e Executar a Aplicação**
    * Compile o projeto com o Maven:
    ```bash
    mvn clean install
    ```
    * Inicie a aplicação com o plugin do Spring Boot:
    ```bash
    mvn spring-boot:run
    ```
    O servidor será iniciado na porta `8080`.

### Acessando a Aplicação
* **Interface Web (Página de Login):**
    [http://localhost:8080/](http://localhost:8080/)
* **Documentação da API (Swagger UI):**
    [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Testes Automatizados (Postman):**
    Um guia completo está disponível no arquivo `TESTING_GUIDE.md` na raiz do projeto.
---
