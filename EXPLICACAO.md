# 🎓 Seminário Back-end — API REST com Spring Boot

> **O projeto em uma frase:** Uma API de mensagens com operações CRUD completas, construída em Java com Spring Boot, que roda com um único comando e não exige nenhuma instalação de banco de dados.

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

Essas 4 operações formam o **CRUD** — Create, Read, Update, Delete — o conjunto básico de qualquer sistema que persiste dados.

---

## 🧱 As Peças do Projeto

O projeto é composto por **3 tecnologias principais** que trabalham juntas:

```
┌─────────────────────────────────────────────────────┐
│  SPRING BOOT  →  Framework que une tudo             │
│  GRADLE       →  Baixa as libs e compila o projeto  │
│  H2           →  Banco de dados em memória          │
└─────────────────────────────────────────────────────┘
```

---

## 🐘 Gradle — O Gerenciador de Build

Antes de qualquer código rodar, o **Gradle** entra em ação. Ele é o responsável por:

1. **Baixar** todas as bibliotecas declaradas no `build.gradle`
2. **Compilar** os arquivos `.java` em bytecode
3. **Empacotar** tudo em um `.jar` executável

As dependências declaradas no projeto são:

```groovy
// build.gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'       // API REST + Tomcat
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa' // Banco de dados
    runtimeOnly    'com.h2database:h2'                                      // Banco em memória
}
```

> 💡 Sem o Gradle, seria preciso baixar cada `.jar` manualmente e garantir que as versões fossem compatíveis — tarefa trabalhosa e propensa a erros.

---

## 🍃 Spring Boot — O Framework

**Spring Boot** é um framework Java que elimina toda a configuração manual que o desenvolvimento back-end exigia antes. Ele segue o princípio de **"convention over configuration"**: já vem com padrões inteligentes prontos, o desenvolvedor só configura o que for diferente do padrão.

### O ponto de entrada da aplicação

```java
@SpringBootApplication  // ← Uma anotação que ativa TUDO
public class SeminarioBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeminarioBackendApplication.class, args);
    }
}
```

Essa única anotação faz o Spring Boot:
- **Escanear** todos os componentes do projeto (`@ComponentScan`)
- **Auto-configurar** as libs detectadas — Tomcat, JPA, H2 (`@EnableAutoConfiguration`)
- **Iniciar** o servidor na porta 8081

---

## 📂 Arquitetura em Camadas

O projeto segue a arquitetura padrão de APIs Spring Boot, com **3 camadas** bem definidas. Cada camada tem uma única responsabilidade:

```
  CLIENTE (Postman / curl / front-end)
       │  requisição HTTP + JSON
       ▼
┌─────────────────────────────┐
│     MensagemController      │  ← Camada Web: recebe a requisição,
│  @RestController            │    chama o repositório e devolve a resposta
└─────────────┬───────────────┘
              │ chama
              ▼
┌─────────────────────────────┐
│     MensagemRepository      │  ← Camada de Dados: interface que o Spring
│  extends JpaRepository      │    implementa automaticamente (sem SQL)
└─────────────┬───────────────┘
              │ persiste
              ▼
┌─────────────────────────────┐
│       Banco H2              │  ← Banco em memória, auto-configurado
│     Tabela: MENSAGEM        │    (existe enquanto a aplicação roda)
└─────────────────────────────┘
```

---

## 🗂️ Explicação Arquivo por Arquivo

### `Mensagem.java` — A Entidade (Modelo de dados)

```java
@Entity  // ← Instrui o JPA a criar a tabela MENSAGEM no banco
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado (1, 2, 3...)
    private Long id;

    private String conteudo; // O texto da mensagem
}
```

**O que o Spring Boot faz aqui:** A anotação `@Entity` dispara a criação automática da tabela no banco H2 ao iniciar a aplicação. Sem o Spring Boot, seria necessário criar a tabela com SQL manualmente.

---

### `MensagemRepository.java` — O Repositório (Acesso ao banco)

```java
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    // Nada aqui — o Spring Boot implementa tudo automaticamente!
}
```

**O que o Spring Boot faz aqui:** Ao estender `JpaRepository`, o Spring Data JPA **gera automaticamente** os seguintes métodos — sem uma linha de SQL:

| Método gerado | O que faz |
|---|---|
| `findAll()` | `SELECT * FROM mensagem` |
| `findById(id)` | `SELECT * FROM mensagem WHERE id = ?` |
| `save(obj)` | `INSERT` ou `UPDATE` |
| `deleteById(id)` | `DELETE FROM mensagem WHERE id = ?` |
| `existsById(id)` | `SELECT COUNT(*) FROM mensagem WHERE id = ?` |

---

### `MensagemController.java` — O Controller (Ponto de entrada HTTP)

O controller recebe as requisições HTTP e orquestra as respostas. Cada método corresponde a um endpoint da API:

```java
@RestController               // Marca como controller REST — respostas em JSON automático
@RequestMapping("/api/mensagens") // Prefixo de todas as rotas
public class MensagemController {

    @Autowired // Spring injeta o repositório — sem precisar de "new MensagemRepository()"
    public MensagemController(MensagemRepository repository) {
        this.repository = repository;
    }

    // GET /api/mensagens → retorna lista de todas as mensagens
    @GetMapping
    public List<Mensagem> listarTodas() {
        return repository.findAll();
    }

    // POST /api/mensagens → cria nova mensagem com o JSON do body
    @PostMapping
    public ResponseEntity<Mensagem> criar(@RequestBody MensagemDto dto) {
        Mensagem salva = repository.save(new Mensagem(dto.conteudo()));
        return new ResponseEntity<>(salva, HttpStatus.CREATED); // HTTP 201
    }

    // PUT /api/mensagens/{id} → atualiza a mensagem com o ID informado
    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(@PathVariable Long id, @RequestBody MensagemDto dto) {
        return repository.findById(id)
            .map(m -> { m.setConteudo(dto.conteudo()); return ResponseEntity.ok(repository.save(m)); })
            .orElse(ResponseEntity.notFound().build()); // HTTP 404 se não existir
    }

    // DELETE /api/mensagens/{id} → remove a mensagem com o ID informado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build(); // HTTP 404
    }
}
```

**Anotações explicadas:**

| Anotação | O que faz |
|---|---|
| `@RestController` | Toda resposta do método vira JSON automaticamente |
| `@RequestMapping` | Define o prefixo da rota (`/api/mensagens`) |
| `@GetMapping` / `@PostMapping` etc. | Liga o método HTTP ao método Java |
| `@RequestBody` | Converte o JSON da requisição em objeto Java automaticamente |
| `@PathVariable` | Captura o `{id}` da URL e passa como parâmetro |
| `@Autowired` | O Spring injeta a dependência sem precisar de `new` |

---

## 💡 Por que Spring Boot? — O Antes e Depois

### Configuração do servidor

**Com Spring Boot:** nada a fazer — o Tomcat sobe automaticamente.

**Sem Spring Boot:** criar `web.xml` + instalar Tomcat separadamente + fazer deploy `.war`:
```xml
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

### Acesso ao banco

**Com Spring Boot:** `repository.findAll()` — uma linha.

**Sem Spring Boot:** instanciar `EntityManager`, escrever JPQL, gerenciar transação e serializar JSON manualmente:
```java
// Apenas para o GET — precisaria repetir para POST, PUT e DELETE
EntityManager em = emf.createEntityManager();
List<Mensagem> lista = em.createQuery("SELECT m FROM Mensagem m", Mensagem.class).getResultList();
// + montar o JSON manualmente com StringBuilder...
em.close();
```

### Resumo da diferença

| | **Com Spring Boot** | **Sem Spring Boot** |
|---|---|---|
| Servidor | Automático e embutido | `web.xml` + Tomcat externo |
| Banco de dados | `application.properties` | `persistence.xml` + SQL manual |
| Acesso ao banco | `repository.findAll()` | JPQL + `EntityManager` |
| JSON | Automático (`@RequestBody`) | Parse manual do stream HTTP |
| Injeção de dependência | `@Autowired` | `new Objeto()` manual |

---

## ▶️ Como Rodar

```powershell
cd seminario-backend
npm start
```

| URL | Para quê |
|---|---|
| `http://localhost:8081/api/mensagens` | Endpoint principal da API |
| `http://localhost:8081/h2-console` | Interface visual do banco H2 |

**Credenciais do H2 Console:**
- JDBC URL: `jdbc:h2:mem:seminariodb`
- User: `sa`
- Senha: *(em branco)*
