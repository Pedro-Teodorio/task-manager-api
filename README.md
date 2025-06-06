# Gerenciador de Tarefas

Uma API RESTful para gerenciar **Usuários**, **Projetos** e **Tarefas**, construída com Quarkus.  
Oferece duas versões de contrato:

- **v1** – Endpoints básicos (CRUD puro sem cross-cutting)
- **v2** – Recursos da v1 + autenticação, rate-limiting, idempotência, validação, CORS e documentação OpenAPI com Swagger-UI e seletor de versão

---

## 🔧 Tecnologias

- Java 17
- Quarkus
- Hibernate ORM / Panache
- Bean Validation (Jakarta Validation)
- SmallRye OpenAPI + Swagger-UI
- MicroProfile Fault Tolerance (`@RateLimit`)
- H2 (em memória para dev)

---

## 🚀 Funcionalidades Principais

### v1 – Endpoints Básicos

- CRUD de **Usuários**  (`/api/v1/usuarios`)
- CRUD de **Projetos**  (`/api/v1/projetos`)
- CRUD de **Tarefas**   (`/api/v1/tarefas`)
- Filtragem de Tarefas por status, usuário e projeto
- Associação de usuários a tarefas

### v2 – Tudo da v1 + Cross-cutting Concerns

1. **Autenticação por API-Key**
    - Header `x-api-key` obrigatório (configurado em `application.properties`)
2. **Rate-Limiting**
    - Com MicroProfile Fault Tolerance: `@RateLimiter(requests=100, timePeriod=1, timeUnit=MINUTES)`
3. **Idempotência**
    - Header `Idempotency-Key` em POST/PUT
    - Requisições duplicadas retornam mensagem informativa sem reprocessar
4. **Validação de Payload**
    - Bean Validation (`@NotNull`, `@Email`, `@Size`)
    - `ValidationExceptionMapper` retorna JSON com lista de erros
5. **Tratamento Centralizado de Erros**
    - `GenericExceptionMapper` para exceções não previstas (500)
6. **CORS**
    - Configurado em `application.properties` (`quarkus.http.cors.*`)
7. **Documentação OpenAPI**
    - **Swagger-UI** disponível em `/swagger`
    

---

## 🏁 Como Rodar

1. Clone o repositório
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-tarefas.git
   cd gerenciador-tarefas
   ```

2. Configure `application.properties` (exemplo em `src/main/resources/application.properties`):

```properties
   # Chave de API
   app.api-key=MINHA_CHAVE_SECRETA_V2
   
   # CORS
   quarkus.http.cors=true
   quarkus.http.cors.origins=*
   quarkus.http.cors.methods=GET,POST,PUT,DELETE,PATCH,OPTIONS
   quarkus.http.cors.headers=Content-Type,Accept,Origin,x-api-key,Idempotency-Key
   
   # OpenAPI / Swagger-UI
   quarkus.swagger-ui.path=/swagger
   quarkus.swagger-ui.always-include=true
   quarkus.swagger-ui.title=Task Manager API
   quarkus.swagger-ui.footer=Task Manager - Powered by Quarkus
   quarkus.swagger-ui.doc-expansion=none
```

3. Execute em modo dev
   ```bash
   ./mvnw quarkus:dev
   ```
   ou empacote e execute jar
   ```bash
   ./mvnw package
   java -jar target/quarkus-app/quarkus-run.jar
   ```

4. Acesse
    - API v1:  `http://localhost:8080/api/v1/...`
    - API v2:  `http://localhost:8080/api/v2/...`
    - Swagger-UI:  
      `http://localhost:8080/swagger-ui` (seletor de versão no topo)
    - OpenAPI v1: `/openapi/v1`
    - OpenAPI v2: `/openapi/v2.json`

---

## 📋 Endpoints Resumidos

### v1 (sem autenticação)

| Método | Caminho               | Descrição                          |
|--------|-----------------------|------------------------------------|
| GET    | /api/v1/usuarios      | Lista todos usuários               |
| POST   | /api/v1/usuarios      | Cria usuário                       |
| GET    | /api/v1/usuarios/{id} | Busca usuário por ID               |
| PUT    | /api/v1/usuarios/{id} | Atualiza usuário                   |
| DELETE | /api/v1/usuarios/{id} | Remove usuário                     |
| …      | /api/v1/projetos      | CRUD Projetos                      |
| …      | /api/v1/tarefas       | CRUD Tarefas + filtros/associações |

### v2 (com API-Key e extras)

| Método | Caminho                      | Headers                             | Descrição                               |
| ------ | ---------------------------- | ----------------------------------- | --------------------------------------- |
| GET    | /api/v2/usuarios             | x-api-key                           | Lista usuários                          |
| POST   | /api/v2/usuarios             | x-api-key, Idempotency-Key          | Cria usuário (idempotente)             |
| PUT    | /api/v2/usuarios/{id}        | x-api-key, Idempotency-Key          | Atualiza usuário (idempotente)         |
| DELETE | /api/v2/usuarios/{id}        | x-api-key                           | Remove usuário                          |
| GET    | /api/v2/usuarios/{id}/projetos | x-api-key                         | Lista projetos do usuário               |
| …      | /api/v2/projetos             | x-api-key, Idempotency-Key, @RateLimiter | CRUD Projetos                        |
| …      | /api/v2/tarefas              | x-api-key, Idempotency-Key, @RateLimiter | CRUD Tarefas + filtros + associação  |

---

## 📑 Documentação OpenAPI com Seletor de Versão

Abra `http://localhost:8080/swagger` para acessar a documentação interativa.

---

## 📬 Testes Rápidos

1. Sem `x-api-key` → **401 Unauthorized**
2. Com `x-api-key` válido → **200 OK**
3. Requisição POST/PUT com mesma `Idempotency-Key` → mensagem de idempotência
4. Mais de 100 requisições em 1 minuto → **429 Too Many Requests**
5. Payload inválido (ex.: email em branco) → **400 Bad Request** com lista de erros

---

## 📂 Importar no Postman

| Ação                           | Link                                                                                                | Descrição                          |
|--------------------------------|-----------------------------------------------------------------------------------------------------|------------------------------------|
| Download Collection Postman V1 | [Baixar Collection do Postman](./src/main/resources/collections/Gerenciador de Tarefas API V1.json) | Collection para testar a V1 da API |
| Download Collection Postman V2 | [Baixar Collection do Postman](./src/main/resources/collections/Gerenciador de Tarefas API V2.json)     | Collection para testar a V1 da API |


---

Feito com ❤️ por **Pedro-Teodorio**.  