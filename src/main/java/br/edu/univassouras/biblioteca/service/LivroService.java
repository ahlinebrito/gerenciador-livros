package br.edu.univassouras.biblioteca.service;

import br.edu.univassouras.biblioteca.exception.LivroNaoEncontradoException;
import br.edu.univassouras.biblioteca.model.Livro;
import br.edu.univassouras.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio do gerenciamento de livros.
 */
@Service
public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    /** 1. Cadastrar */
    public Livro cadastrar(Livro livro) {
        livro.setId(null); // garante que será criado um novo registro
        return repository.save(livro);
    }

    /** 2. Listar */
    public List<Livro> listar() {
        return repository.findAll();
    }

    /** 3. Consultar por id */
    public Livro consultar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));
    }

    /** 4. Atualizar */
    public Livro atualizar(Long id, Livro dados) {
        Livro existente = consultar(id);
        existente.atualizarDados(dados);
        return repository.save(existente);
    }

    /** 5. Excluir */
    public void excluir(Long id) {
        Livro existente = consultar(id);
        repository.delete(existente);
    }
}
