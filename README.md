# Gerenciador de Livros — Biblioteca Pessoal

Trabalho prático de desenvolvimento de aplicativo com **Java 17**, **Spring Boot 3** e **IntelliJ IDEA**.
A aplicação é uma API REST que permite cadastrar, listar, consultar, atualizar e excluir livros de uma biblioteca pessoal.

## Tecnologias

- Java 17
- Spring Boot 3.3 (Web, Data JPA, Validation)
- Banco de dados H2 (em memória)
- JUnit 5, Mockito e MockMvc (testes)
- Maven

## Estrutura do projeto

```
src/main/java/br/edu/univassouras/biblioteca
├── BibliotecaApplication.java      → classe principal (inicia o Spring Boot)
├── model
│   ├── Livro.java                  → entidade (id, título, autor, gênero, ano, status)
│   └── StatusLeitura.java          → enum: NAO_LIDO, LENDO, LIDO
├── repository
│   └── LivroRepository.java        → acesso ao banco (Spring Data JPA)
├── service
│   └── LivroService.java           → regras de negócio (CRUD)
├── controller
│   └── LivroController.java        → endpoints REST
└── exception
    ├── LivroNaoEncontradoException.java
    └── GlobalExceptionHandler.java → respostas de erro 400/404
```

## Conceitos de Programação Orientada a Objetos aplicados

- **Classes e objetos:** `Livro` é a classe que modela o domínio; cada livro cadastrado é um objeto dessa classe.
- **Atributos:** `id`, `titulo`, `autor`, `genero`, `anoPublicacao`, `status`.
- **Métodos:** getters/setters, `atualizarDados()` em `Livro`, e os métodos de CRUD em `LivroService`.
- **Encapsulamento:** atributos `private` acessados por métodos públicos.
- **Herança:** `LivroNaoEncontradoException` estende `RuntimeException`; `LivroRepository` estende `JpaRepository`.
- **Abstração / interfaces:** o repositório é uma interface cuja implementação é gerada pelo Spring.
- **Injeção de dependência:** o Spring cria e injeta o `LivroService` no controller e o `LivroRepository` no service pelo construtor.
- **Separação em camadas:** Controller → Service → Repository → Model.

## Endpoints

| Funcionalidade | Método | URL                  | Resposta de sucesso |
|----------------|--------|----------------------|---------------------|
| Cadastrar      | POST   | `/api/livros`        | 201 Created         |
| Listar         | GET    | `/api/livros`        | 200 OK              |
| Consultar      | GET    | `/api/livros/{id}`   | 200 OK              |
| Atualizar      | PUT    | `/api/livros/{id}`   | 200 OK              |
| Excluir        | DELETE | `/api/livros/{id}`   | 204 No Content      |

Exemplo de corpo (JSON) para cadastrar ou atualizar:

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "genero": "Romance",
  "anoPublicacao": 1899,
  "status": "LIDO"
}
```

Valores aceitos em `status`: `NAO_LIDO`, `LENDO`, `LIDO`.

## Como executar no IntelliJ IDEA

1. **File → Open** e selecione a pasta do projeto (onde está o `pom.xml`). O IntelliJ baixa as dependências do Maven automaticamente.
2. Confirme o JDK em **File → Project Structure → SDK** (Java 17 ou superior).
3. Abra `BibliotecaApplication.java` e clique no ▶ verde ao lado do método `main`.
4. A API fica disponível em `http://localhost:8080/api/livros`.

Pelo terminal (alternativa): `mvn spring-boot:run`

## Testes

### Testes automatizados
No IntelliJ, clique com o botão direito na pasta `src/test/java` → **Run 'All Tests'**.
Ou pelo terminal: `mvn test`

- `LivroServiceTest` — testes unitários da camada de serviço (7 testes, com Mockito).
- `LivroControllerTest` — testes de integração dos endpoints com banco H2 (7 testes).

Os testes cobrem as cinco funcionalidades e também os casos de erro (livro inexistente → 404, dados inválidos → 400).

### Testes manuais
Com a aplicação rodando, abra o arquivo `requisicoes.http` no IntelliJ e clique no ▶ ao lado de cada requisição.
Também é possível usar Postman ou Insomnia.

O banco pode ser visualizado em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:biblioteca`, usuário `sa`, sem senha).
Por ser em memória, os dados são apagados quando a aplicação é encerrada.
