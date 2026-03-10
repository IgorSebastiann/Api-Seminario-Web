# 🎓 Explicação do Projeto — Seminário Back-end

Este projeto é uma **Prova de Conceito (PoC)** de uma **API REST** construída com **Java + Spring Boot**. O objetivo é demonstrar como o Spring Boot simplifica e agiliza a criação de um back-end moderno e funcional.

---

## 🍃 O que é o Spring Boot?

**Spring Boot** é um framework Java criado pela empresa **Pivotal (VMware)** que torna o desenvolvimento de aplicações back-end muito mais rápido e simples.

Ele é construído em cima do **Spring Framework** — um dos ecossistemas mais consolidados do mundo Java — porém elimina toda a configuração manual e verbosa que o Spring puro exigia.

### Por que usar Spring Boot?

| Sem Spring Boot | Com Spring Boot |
|---|---|
| Configuração manual de servidores (Tomcat, Jetty) | Servidor embutido — roda com um único comando |
| Arquivos XML extensos de configuração | Configuração por anotações e `application.properties` |
| Gerenciamento manual de dependências e versões | **Starters** já agrupam e compatibilizam as dependências |
| Código boilerplate para conectar banco de dados | Spring Data JPA gera os métodos automaticamente |

> 💡 **Resumo:** Spring Boot segue o princípio de **"convention over configuration"** — ele já vem com padrões inteligentes configurados, o desenvolvedor só muda o que precisar.

---

## 🍃 Principais Recursos do Spring Boot usados neste projeto

### 1. Auto-configuração (`@SpringBootApplication`)
```java
@SpringBootApplication
public class SeminarioBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeminarioBackendApplication.class, args);
    }
}
```
Esta única anotação ativa **3 funcionalidades ao mesmo tempo**:
- `@Configuration` — habilita a configuração da aplicação
- `@EnableAutoConfiguration` — o Spring detecta as libs no projeto e se configura sozinho
- `@ComponentScan` — escaneia e registra todos os beans (componentes) da aplicação

---

### 2. Spring Web — Criando a API REST
A dependência `spring-boot-starter-web` adiciona tudo o necessário para uma API HTTP, incluindo o servidor **Tomcat embutido**.

As principais anotações utilizadas:

| Anotação | Função |
|---|---|
| `@RestController` | Marca a classe como controlador REST — retorna JSON automaticamente |
| `@RequestMapping("/api/mensagens")` | Define a rota base de todos os endpoints |
| `@GetMapping` | Mapeia requisições HTTP GET |
| `@PostMapping` | Mapeia requisições HTTP POST |
| `@PutMapping("/{id}")` | Mapeia requisições HTTP PUT com parâmetro na URL |
| `@DeleteMapping("/{id}")` | Mapeia requisições HTTP DELETE com parâmetro na URL |
| `@RequestBody` | Converte o JSON da requisição em objeto Java automaticamente |
| `@PathVariable` | Captura um valor da URL (ex: o `{id}`) |

---

### 3. Spring Data JPA — Banco de Dados sem SQL
A dependência `spring-boot-starter-data-jpa` integra o **Hibernate** (ORM) e simplifica todo o acesso ao banco.

Com uma única interface, o Spring Boot gera automaticamente todas as operações de banco:

```java
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    // Nenhum código SQL necessário!
    // O Spring Boot já fornece: save, findAll, findById, deleteById, existsById...
}
```

A anotação `@Entity` na classe `Mensagem` instrui o JPA a criar a tabela no banco automaticamente:

```java
@Entity
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id;
    private String conteudo;
}
```

---

### 4. Injeção de Dependência (`@Autowired`)
O Spring Boot gerencia o ciclo de vida dos objetos. Em vez de criar instâncias manualmente com `new`, o framework **injeta** automaticamente as dependências:

```java
@Autowired
public MensagemController(MensagemRepository repository) {
    this.repository = repository; // Spring injeta o repositório automaticamente
}
```

---

### 5. H2 — Banco de Dados em Memória
O Spring Boot detecta o H2 no projeto e o configura automaticamente, sem nenhuma instalação externa. Os dados existem enquanto a aplicação está rodando — ideal para demonstrações.

---

## 📂 Arquitetura em Camadas

```
┌─────────────────────────────────────┐
│         Cliente (curl/Postman)       │
└──────────────────┬───────────────────┘
                   │ HTTP Request
┌──────────────────▼───────────────────┐
│        MensagemController            │  ← Camada Web (Spring MVC)
│  @RestController + @RequestMapping   │
└──────────────────┬───────────────────┘
                   │ chama
┌──────────────────▼───────────────────┐
│        MensagemRepository            │  ← Camada de Dados (Spring Data JPA)
│    extends JpaRepository<...>        │
└──────────────────┬───────────────────┘
                   │ persiste
┌──────────────────▼───────────────────┐
│        Banco de Dados H2             │  ← Banco em memória (auto-configurado)
│       Tabela: MENSAGEM               │
└─────────────────────────────────────┘
```

---

## 🔗 Endpoints da API (CRUD)

| Método | Rota | Ação |
|---|---|---|
| `GET` | `/api/mensagens` | Lista todas as mensagens |
| `POST` | `/api/mensagens` | Cria uma nova mensagem |
| `PUT` | `/api/mensagens/{id}` | Atualiza uma mensagem |
| `DELETE` | `/api/mensagens/{id}` | Remove uma mensagem |

---

## ▶️ Como Rodar

```powershell
cd seminario-backend
npm start
```

- API: **http://localhost:8081/api/mensagens**
- Console H2: **http://localhost:8081/h2-console** (JDBC URL: `jdbc:h2:mem:seminariodb` | User: `sa` | Senha: em branco)
