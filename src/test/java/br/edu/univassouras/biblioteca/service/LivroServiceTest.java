package br.edu.univassouras.biblioteca.service;

import br.edu.univassouras.biblioteca.exception.LivroNaoEncontradoException;
import br.edu.univassouras.biblioteca.model.Livro;
import br.edu.univassouras.biblioteca.model.StatusLeitura;
import br.edu.univassouras.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários da camada de serviço (o repositório é simulado com Mockito).
 */
@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroService service;

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro("Dom Casmurro", "Machado de Assis", "Romance", 1899, StatusLeitura.LIDO);
        livro.setId(1L);
    }

    @Test
    @DisplayName("Cadastrar deve salvar um novo livro")
    void deveCadastrarLivro() {
        when(repository.save(any(Livro.class))).thenReturn(livro);

        Livro salvo = service.cadastrar(new Livro("Dom Casmurro", "Machado de Assis", "Romance", 1899, StatusLeitura.LIDO));

        assertEquals(1L, salvo.getId());
        assertEquals("Dom Casmurro", salvo.getTitulo());
        verify(repository).save(any(Livro.class));
    }

    @Test
    @DisplayName("Listar deve retornar todos os livros")
    void deveListarLivros() {
        Livro outro = new Livro("1984", "George Orwell", "Distopia", 1949, StatusLeitura.LENDO);
        when(repository.findAll()).thenReturn(List.of(livro, outro));

        List<Livro> livros = service.listar();

        assertEquals(2, livros.size());
    }

    @Test
    @DisplayName("Consultar deve retornar o livro quando o id existe")
    void deveConsultarLivroExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(livro));

        Livro encontrado = service.consultar(1L);

        assertEquals("Machado de Assis", encontrado.getAutor());
    }

    @Test
    @DisplayName("Consultar deve lançar exceção quando o id não existe")
    void deveLancarExcecaoAoConsultarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LivroNaoEncontradoException.class, () -> service.consultar(99L));
    }

    @Test
    @DisplayName("Atualizar deve alterar os dados do livro")
    void deveAtualizarLivro() {
        when(repository.findById(1L)).thenReturn(Optional.of(livro));
        when(repository.save(any(Livro.class))).thenAnswer(inv -> inv.getArgument(0));

        Livro novosDados = new Livro("Dom Casmurro", "Machado de Assis", "Clássico", 1899, StatusLeitura.LENDO);
        Livro atualizado = service.atualizar(1L, novosDados);

        assertEquals("Clássico", atualizado.getGenero());
        assertEquals(StatusLeitura.LENDO, atualizado.getStatus());
        assertEquals(1L, atualizado.getId());
    }

    @Test
    @DisplayName("Excluir deve remover o livro existente")
    void deveExcluirLivro() {
        when(repository.findById(1L)).thenReturn(Optional.of(livro));

        service.excluir(1L);

        verify(repository).delete(livro);
    }

    @Test
    @DisplayName("Excluir deve lançar exceção quando o id não existe")
    void deveLancarExcecaoAoExcluirInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LivroNaoEncontradoException.class, () -> service.excluir(99L));
        verify(repository, never()).delete(any());
    }
}
