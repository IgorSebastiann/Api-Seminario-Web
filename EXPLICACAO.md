# 🎓 Seminário Back-end — API REST com Spring Boot

> **O projeto em uma frase:** Uma API de mensagens com operações CRUD completas, construída em Java com Spring Boot, focada exclusivamente na camada Web (Controllers e DTOs), gerenciando dados temporariamente na memória, e que roda com um único comando.

---

## 📌 Visão Geral — O que foi construído

Este projeto é uma **API REST** que gerencia mensagens. O cliente (Postman, curl, front-end) envia requisições HTTP e a API responde em **JSON**.

As operações disponíveis são:

| Método HTTP | Rota | O que faz |
|---|---|---|
| `GET` | `/api/mensagens` | Lista todas as mensagens salvas |
| `POST` | `/api/mensagens` | Cria uma nova mensagem |
| `PUT` | `/api/mensagens/{id}` | Atualiza o conteúdo de uma mensagem |
| `DELETE` | `/api/mensagens/{id}` | Remove uma mensagem |

Essas 4 operações formam o **CRUD** — Create, Read, Update, Delete — o conjunto básico de qualquer sistema de APIs.

---

## 🧱 As Peças do Projeto

O projeto é composto por **2 ferramentas principais** que trabalham juntas:

```
┌─────────────────────────────────────────────────────┐
│  SPRING BOOT  →  Framework que recebe as requisições│
│  GRADLE       →  Baixa as libs e compila o projeto  │
└─────────────────────────────────────────────────────┘
```

---

## 🐘 Gradle — O Gerenciador de Build

Antes de qualquer código rodar, o **Gradle** entra em ação. Ele é o responsável por:

1. **Baixar** todas as bibliotecas declaradas no `build.gradle`
2. **Compilar** os arquivos `.java` em bytecode
3. **Empacotar** tudo em um `.jar` executável

A dependência principal declarada no projeto é:

```groovy
// build.gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web' // API REST + Servidor Tomcat Embutido
}
```

> 💡 Sem o Gradle, seria preciso baixar cada `.jar` manualmente e garantir que as versões fossem compatíveis — tarefa trabalhosa e propensa a erros.

---

## 🍃 Spring Boot — O Framework Web

**Spring Boot** é um framework Java que elimina toda a configuração manual que o desenvolvimento back-end exigia antes. Ele segue o princípio de **"convention over configuration"**: já vem com padrões inteligentes prontos (como converter respostas para JSON sozinho), o desenvolvedor só configura o que for diferente do padrão.

### O ponto de entrada da aplicação

```java
@SpringBootApplication  // ← Uma anotação que ativa a auto-configuração web
public class SeminarioBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeminarioBackendApplication.class, args);
    }
}
```

Essa única anotação faz o Spring Boot:
- **Escanear** todos os componentes do projeto (`@ComponentScan`)
- **Iniciar o Tomcat** (servidor web embutido) na porta 8081

---

## 📂 Arquitetura (Foco na Camada Web)

Neste projeto de prova de conceito, focamos apenas na construção de **Controllers e DTOs**, sem as outras dependências mais pesadas (como banco de dados).

```
  CLIENTE (Postman / curl / front-end)
       │  requisição HTTP + JSON
       ▼
┌─────────────────────────────┐
│     MensagemController      │  ← Camada Web: recebe a requisição,
│  @RestController            │    valida o DTO, faz as regras
└─────────────┬───────────────┘    e devolve uma resposta HTTP
              │ gerencia
              ▼
┌─────────────────────────────┐
│     Lista na Memória        │  ← Simulação de um banco de dados usando
│  ArrayList<Mensagem>        │    variáveis e Listas do construtor Java
└─────────────────────────────┘
```

---

## 🗂️ Explicação Arquivo por Arquivo

### `Mensagem.java` — O Modelo de dados

A classe mais simples do projeto. Guarda os campos que vamos trabalhar na memória.

```java
public class Mensagem {

    private Long id;
    private String conteudo; 
    
    // Construtores, Getters e Setters...
}
```

---

### `MensagemDto.java` — O Objeto de Transferência (DTO - Record)

```java
public record MensagemDto(String conteudo) {
}
```

**Por que usar DTOs?**
O DTO (Data Transfer Object) é um padrão usado para filtrar quais dados entram e saem da sua API pela internet. Com o novo recurso de `records` do Java (lançado na versão 14), o DTO fica super limpo e conciso, ideal para receber o JSON `'{"conteudo": "Meu texto"}'` direto das requisições web.

---

### `MensagemController.java` — O Ponto Principal da API

O controller recebe as requisições HTTP e orquestra as respostas. Cada método corresponde a um endpoint:

```java
@RestController               // Marca como controller REST — as respostas já viram JSON
@RequestMapping("/api/mensagens") // Prefixo de todas as rotas desta classe
public class MensagemController {

    // Simulação do nosso "banco" temporário
    private final List<Mensagem> mensagensEmMemoria = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    // GET /api/mensagens → retorna tudo
    @GetMapping
    public List<Mensagem> listarTodas() {
        return mensagensEmMemoria; 
    }

    // POST /api/mensagens → cria nova usando o JSON que chegou
    @PostMapping
    public ResponseEntity<Mensagem> criar(@RequestBody MensagemDto dto) {
        Mensagem nova = new Mensagem(dto.conteudo());
        nova.setId(contadorId.getAndIncrement()); // Auto-incremento do Java
        mensagensEmMemoria.add(nova);
        return new ResponseEntity<>(nova, HttpStatus.CREATED); // Devolve HTTP 201 (Created)
    }

    // PUT /api/mensagens/{id} → atualiza
    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(@PathVariable Long id, @RequestBody MensagemDto dto) {
        Optional<Mensagem> mensagem = mensagensEmMemoria.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();

        if (mensagem.isPresent()) {
            mensagem.get().setConteudo(dto.conteudo());
            return new ResponseEntity<>(mensagem.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devolve 404 pro cliente
    }

    // DELETE /api/mensagens/{id} → deleta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = mensagensEmMemoria.removeIf(m -> m.getId().equals(id));
        if (removido) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
```

**Anotações explicadas:**

| Anotação | O que faz |
|---|---|
| `@RestController` | Informa que não devolveremos HTML, e sim JSON (API) |
| `@RequestMapping` | Define o prefixo da rota (`/api/mensagens`) |
| `@GetMapping` / `@PostMapping` etc. | Liga o método HTTP do navegador àquele método Java específico |
| `@RequestBody` | O Spring Boot converte a string JSON solta lá do cliente de volta para um objeto Java (o nosso `MensagemDto`) |
| `@PathVariable` | Captura o `{id}` que o usuário botou na URL para podermos procurar a mensagem correta |

---

## 💡 Por que Spring Boot? — O Antes e Depois

### Configuração do servidor web

**Com Spring Boot:** nada a fazer — o servidor web chamado Tomcat sobe automaticamente.

**Sem Spring Boot:** você teria que configurar vários arquivos complicados como o `web.xml`, baixar o Tomcat por fora da aplicação, e programar `Servlets` gigantescos sem a estrutura pronta de ResponseEntities:
```xml
<!-- Exemplo doloroso que o Spring poupou você -->
<web-app>
    <servlet>
        <servlet-name>MensagemServlet</servlet-name>
        <servlet-class>com.exemplo.MensagemServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>MensagemServlet</servlet-name>
        <url-pattern>/api/mensagens/*</url-pattern>
    </servlet-mapping>
</web-app>
```

### Serialização JSON e Tratamento da Requisição

**Com Spring Boot:** `@RequestBody MensagemDto dto` — e o Spring já traduz a string pra objeto com os campos no lugar certo! E para devolver pro navegador é só usar um simples `return objeto;`.

**Sem Spring Boot:** Você abriria um stream de dados pesado vindo do InputStreamHTTP, e passaria toda a requisição para uma biblioteca extra como o Jackson parsear isso na unha, campo por campo.

---

## ▶️ Como Rodar a API

```powershell
cd seminario-backend
npm start
```

| URL | Para quê |
|---|---|
| `http://localhost:8081/api/mensagens` | Endereço principal do Controller de Mensagens |

*(Esta aplicação armazena os dados em memória RAM. Se você cancelar o terminal apertando CTRL+C e rodar de novo, a lista de mensagens começará vazia, voltando apenas com as operações da sua sessão!)*
