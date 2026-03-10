# Seminário Back-end: Prova de Conceito com Java & Spring Boot

Esta é uma demonstração de uma API REST simples utilizando **Spring Boot**.
A aplicação é focada apenas na criação de **Controllers** e **DTOs (Records)**, mantendo os dados salvos em memória (sem uso de banco de dados externo) para focar puramente no tráfego das requisições REST.

---
# Habilitar NPM

```bash
npm install
```

# Habilitar Gradle

```bash
gradlew build
```
```bash
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned -Force
```

## 🚀 Como Rodar o Projeto

1. Abra o terminal (ou Prompt de Comando/PowerShell) na pasta do projeto `seminario-backend`.
2. Como a configuração principal está estruturada no arquivo **`package.json`**, execute o comando:
   
   ```bash
   cd seminario-backend
   npm start
   ```
   *(Por baixo dos panos, o script `npm start` executa o wrapper do Gradle: `gradlew bootRun`)*

A aplicação iniciará na porta `8081`.

## 🛠️ Testando a API ("Hello World" Funcional)

Você pode testar a API usando o **cURL**, **Postman**, **Insomnia** ou qualquer outra ferramenta.

### 1. Criar uma nova mensagem (POST)
Abra outro terminal e execute. 

**No Linux / Mac / Git Bash:**
```bash
curl -X POST http://localhost:8081/api/mensagens -H "Content-Type: application/json" -d '{"conteudo": "Olá, Spring Boot!"}'
```

**No Windows (PowerShell):**
*(É necessário usar `curl.exe` e escapar as aspas internas com crase ` )*
```powershell
curl.exe -X POST http://localhost:8081/api/mensagens -H "Content-Type: application/json" -d "{`"conteudo`": `"Olá, Spring Boot!`"}"
```
*Saída esperada:* `{"id":1,"conteudo":"Olá, Spring Boot!"}`

### 2. Listar mensagens (GET)

**No Linux / Mac / Git Bash:**
```bash
curl -X GET http://localhost:8081/api/mensagens
```

**No Windows (PowerShell):**
```powershell
curl.exe -X GET http://localhost:8081/api/mensagens
```
*Saída esperada:* `[{"id":1,"conteudo":"Olá, Spring Boot!"}]`

### 3. Atualizar uma mensagem (PUT)
Neste exemplo, estamos atualizando a mensagem de ID `1`.

**No Linux / Mac / Git Bash:**
```bash
curl -X PUT http://localhost:8081/api/mensagens/1 -H "Content-Type: application/json" -d '{"conteudo": "Mensagem atualizada com sucesso!"}'
```

**No Windows (PowerShell):**
```powershell
curl.exe -X PUT http://localhost:8081/api/mensagens/1 -H "Content-Type: application/json" -d "{`"conteudo`": `"Mensagem atualizada com sucesso!`"}"
```

### 4. Deletar uma mensagem (DELETE)
Neste exemplo, estamos deletando a mensagem de ID `1`.

**No Linux / Mac / Git Bash:**
```bash
curl -X DELETE http://localhost:8081/api/mensagens/1
```

**No Windows (PowerShell):**
```powershell
curl.exe -X DELETE http://localhost:8081/api/mensagens/1
```

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
- Clique em **Send**. Você verá um array JSON com todas as mensagens criadas na sessão atual em memória.

---

## 📂 Arquivos Principais Desta Prova de Conceito
- `build.gradle`: Mostra o "setup" inicial, gerenciador de dependências (Starters Web).
- `MensagemController.java`: Endpoints da nossa API REST (Controller) com a lógica em memória.
- `MensagemDto.java`: Objeto de Transferência de Dados usando o recurso `record` do Java.
- `Mensagem.java`: Classe de modelo representando a Mensagem na aplicação.
