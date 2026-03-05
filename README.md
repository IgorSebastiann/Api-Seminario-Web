# Seminário Back-end: Prova de Conceito com Java & Spring Boot

Esta é uma demonstração de uma API REST simples utilizando **Spring Boot**, **Spring Data JPA** e um banco de dados em memória **H2**.

## 🚀 Como Rodar o Projeto

1. Abra o terminal (ou Prompt de Comando/PowerShell) na pasta do projeto `seminario-backend`.
2. Execute o comando abaixo para compilar e rodar a aplicação usando o Gradle Wrapper embutido:
   
   **No Windows:**
   ```bash
   .\gradlew.bat bootRun
   ```
   **No Mac/Linux:**
   ```bash
   ./gradlew bootRun
   ```

A aplicação iniciará na porta `8081`.

## 🛠️ Testando a API ("Hello World" Funcional)

Você pode testar a API usando o **cURL**, **Postman**, **Insomnia** ou qualquer outra ferramenta.

### 1. Criar uma nova mensagem (POST)
Abra outro terminal e execute:
```bash
curl -X POST http://localhost:8081/api/mensagens -H "Content-Type: application/json" -d "{\"conteudo\": \"Olá, Spring Boot!\"}"
```
*Saída esperada:* `{"id":1,"conteudo":"Olá, Spring Boot!"}`

### 2. Listar mensagens (GET)
```bash
curl -X GET http://localhost:8081/api/mensagens
```
*Saída esperada:* `[{"id":1,"conteudo":"Olá, Spring Boot!"}]`

### Via Postman
Para testar a API com uma interface gráfica mais amigável, você pode usar o Postman:

**1. Criar uma nova mensagem (POST)**
- Na barra de endereços, selecione **POST** e insira a URL: `http://localhost:8081/api/mensagens`
- Vá na aba **Body**, selecione a opção **raw** e mude o dropdown de `Text` para `JSON`.
- Cole o seguinte conteúdo no corpo da requisição:
  ```json
  {
      "conteudo": "Testando via Postman!"
  }
  ```
- Clique em **Send**. A resposta será a mensagem criada com um `id`.

**2. Listar mensagens (GET)**
- Abra uma nova aba, selecione o método **GET** e insira a mesma URL: `http://localhost:8081/api/mensagens`
- Clique em **Send**. Você verá um array JSON com todas as mensagens já criadas no banco.

---

## 🗄️ Console do Banco de Dados H2
Como estamos utilizando o H2, você pode visualizar o banco de dados rodando direto pelo navegador:
1. Acesse: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
2. **JDBC URL:** `jdbc:h2:mem:seminariodb`
3. **User Name:** `sa`
4. Deixe a senha em branco e clique em **Connect**.
5. Você poderá ver a tabela `MENSAGEM` recém-criada através do Spring Data JPA!

## 📂 Arquivos Principais Desta Prova de Conceito
- `build.gradle`: Mostra o "setup" inicial, gerenciador de dependências (Starters Web e Data JPA).
- `MensagemController.java`: Endpoints da nossa API REST (Controller).
- `MensagemDto.java`: Objeto de Transferência de Dados usando o recurso `record` do Java.
- `Mensagem.java`: Entidade para abstração do banco de dados (Spring Data JPA).
- `MensagemRepository.java`: Interface que abstrai a camada de dados (Spring Data JPA).
