package br.edu.univassouras.biblioteca.exception;

/**
 * Lançada quando um livro não é encontrado pelo identificador informado.
 */
public class LivroNaoEncontradoException extends RuntimeException {

    public LivroNaoEncontradoException(Long id) {
        super("Livro com id " + id + " não encontrado");
    }
}
