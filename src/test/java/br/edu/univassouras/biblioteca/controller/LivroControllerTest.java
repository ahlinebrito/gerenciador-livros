package br.edu.univassouras.biblioteca.controller;

import br.edu.univassouras.biblioteca.repository.LivroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração: sobem a aplicação inteira (com banco H2) e
 * fazem requisições HTTP reais aos endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LIVRO_JSON = """
            {
              "titulo": "O Hobbit",
              "autor": "J. R. R. Tolkien",
              "genero": "Fantasia",
              "anoPublicacao": 1937,
              "status": "NAO_LIDO"
            }
            """;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private Long cadastrarLivro() throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LIVRO_JSON))
                .andReturn();
        JsonNode json = objectMapper.readTree(resultado.getResponse().getContentAsString());
        return json.get("id").asLong();
    }

    @Test
    @DisplayName("POST /api/livros cadastra e retorna 201")
    void deveCadastrar() throws Exception {
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LIVRO_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("O Hobbit"))
                .andExpect(jsonPath("$.status").value("NAO_LIDO"));
    }

    @Test
    @DisplayName("POST /api/livros com dados inválidos retorna 400")
    void deveRejeitarCadastroInvalido() throws Exception {
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\": \"\", \"autor\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.titulo").exists())
                .andExpect(jsonPath("$.erros.autor").exists());
    }

    @Test
    @DisplayName("GET /api/livros lista todos os livros")
    void deveListar() throws Exception {
        cadastrarLivro();
        cadastrarLivro();

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/livros/{id} consulta um livro")
    void deveConsultar() throws Exception {
        Long id = cadastrarLivro();

        mockMvc.perform(get("/api/livros/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autor").value("J. R. R. Tolkien"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} inexistente retorna 404")
    void deveRetornar404AoConsultarInexistente() throws Exception {
        mockMvc.perform(get("/api/livros/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Livro com id 9999 não encontrado"));
    }

    @Test
    @DisplayName("PUT /api/livros/{id} atualiza um livro")
    void deveAtualizar() throws Exception {
        Long id = cadastrarLivro();
        String atualizado = LIVRO_JSON.replace("NAO_LIDO", "LIDO");

        mockMvc.perform(put("/api/livros/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("LIDO"));
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} exclui um livro")
    void deveExcluir() throws Exception {
        Long id = cadastrarLivro();

        mockMvc.perform(delete("/api/livros/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/livros/" + id))
                .andExpect(status().isNotFound());
    }
}
