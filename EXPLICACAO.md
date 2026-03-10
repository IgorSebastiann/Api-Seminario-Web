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

## 🐘 Gradle — O Gerenciador de Build

**Gradle** é a ferramenta que **orquestra a construção do projeto**. Antes de rodar uma linha de código, o Gradle:

1. **Baixa as dependências** declaradas (Spring Boot, H2, Hibernate...) do repositório Maven Central
2. **Compila** todos os arquivos `.java` para bytecode
3. **Empacota** tudo em um único `.jar` executável

As dependências do projeto ficam declaradas no `build.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2'
}
```

> 💡 Sem o Gradle, seria necessário baixar cada `.jar` manualmente, garantir que as versões fossem compatíveis entre si e adicioná-los ao classpath — um processo trabalhoso e propenso a erros.

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

---

## ⚔️ Antes vs Depois — Código Real do Projeto

Esta seção mostra o que seria necessário escrever para construir **a mesma API** sem o Spring Boot, usando apenas **Java EE / Servlet puro**.

---

### 🔴 Sem Spring Boot — Configuração do Servidor

Sem o servidor embutido, seria necessário criar um `web.xml` para registrar cada servlet manualmente:

```xml
<!-- web.xml — arquivo de configuração obrigatório -->
<web-app>
    <servlet>
        <servlet-name>MensagemServlet</servlet-name>
        <servlet-class>com.exemplo.seminario.MensagemServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>MensagemServlet</servlet-name>
        <url-pattern>/api/mensagens/*</url-pattern>
    </servlet-mapping>
</web-app>
```

> ⚠️ Além disso, seria preciso instalar o **Tomcat separadamente**, empacotar o projeto como `.war` e fazer o deploy manualmente a cada alteração.

---

### 🔴 Sem Spring Boot — O Controller (Servlet puro)

**Com Spring Boot** — o `MensagemController.java` do projeto tem 63 linhas e cobre os 4 verbos HTTP com anotações simples:

```java
// ✅ COM SPRING BOOT — código atual do projeto
@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    public MensagemController(MensagemRepository repository) {
        this.repository = repository; // Spring injeta automaticamente
    }

    @GetMapping
    public List<Mensagem> listarTodas() {
        return repository.findAll(); // Uma linha — Spring Data cuida do SQL
    }

    @PostMapping
    public ResponseEntity<Mensagem> criar(@RequestBody MensagemDto dto) {
        Mensagem salva = repository.save(new Mensagem(dto.conteudo()));
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }
}
```

**Sem Spring Boot** — o mesmo comportamento exigiria um `HttpServlet` com parse manual de JSON e roteamento na mão:

```java
// ❌ SEM SPRING BOOT — Servlet puro equivalente
@WebServlet("/api/mensagens/*")
public class MensagemServlet extends HttpServlet {

    // Sem injeção de dependência — instância criada manualmente
    private EntityManagerFactory emf;

    @Override
    public void init() {
        // Configuração manual do banco — sem auto-configuração
        emf = Persistence.createEntityManagerFactory("seminarioPU");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        EntityManager em = emf.createEntityManager();

        // SQL manual — sem Spring Data JPA
        List<Mensagem> mensagens = em.createQuery(
            "SELECT m FROM Mensagem m", Mensagem.class
        ).getResultList();

        // Serialização JSON manual — sem Jackson automático
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < mensagens.size(); i++) {
            Mensagem m = mensagens.get(i);
            json.append("{\"id\":").append(m.getId())
                .append(",\"conteudo\":\"").append(m.getConteudo())
                .append("\"}");
            if (i < mensagens.size() - 1) json.append(",");
        }
        json.append("]");
        resp.getWriter().write(json.toString());

        em.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // Parse manual do JSON do corpo da requisição — sem @RequestBody
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        // Extrair "conteudo" do JSON manualmente — sem ObjectMapper automático
        String conteudo = body.toString()
            .replaceAll(".*\"conteudo\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Mensagem nova = new Mensagem(conteudo);
        em.persist(nova);
        em.getTransaction().commit();

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(
            "{\"id\":" + nova.getId() + ",\"conteudo\":\"" + nova.getConteudo() + "\"}"
        );
        em.close();
    }

    // doDelete e doPut teriam que ser implementados com lógica similar...
    // + roteamento manual pela URL para distinguir /api/mensagens/{id}
}
```

---

### 🔴 Sem Spring Boot — Configuração do Banco (persistence.xml)

Em vez do banco ser configurado automaticamente, seria necessário criar um `persistence.xml`:

```xml
<!-- src/main/resources/META-INF/persistence.xml -->
<persistence>
    <persistence-unit name="seminarioPU">
        <class>com.exemplo.seminario.entity.Mensagem</class>
        <properties>
            <property name="javax.persistence.jdbc.driver"   value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.url"      value="jdbc:h2:mem:seminariodb"/>
            <property name="javax.persistence.jdbc.user"     value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="hibernate.hbm2ddl.auto"          value="create-drop"/>
            <property name="hibernate.dialect"               value="org.hibernate.dialect.H2Dialect"/>
        </properties>
    </persistence-unit>
</persistence>
```

Com o Spring Boot, **todo esse conteúdo fica em 3 linhas** no `application.properties` — e boa parte é auto-configurada sem precisar escrever nada.

---

### 📊 Resumo da Comparação

| | **Com Spring Boot** (projeto atual) | **Sem Spring Boot** (Java EE puro) |
|---|---|---|
| Linhas no Controller | ~63 linhas | ~100+ linhas por verbo HTTP |
| Configuração de servidor | Zero — embutido e automático | `web.xml` + instalação do Tomcat |
| Configuração de banco | `application.properties` (3 linhas) | `persistence.xml` (~15 linhas) |
| Parse de JSON | Automático (`@RequestBody`) | Manual (leitura de stream + parsing) |
| Acesso ao banco | `repository.findAll()` | JPQL/SQL explícito + `EntityManager` |
| Roteamento HTTP | `@GetMapping`, `@PostMapping`... | `if/else` na URL dentro do Servlet |
| Injeção de dependência | `@Autowired` | `new MensagemServlet()` manual |
