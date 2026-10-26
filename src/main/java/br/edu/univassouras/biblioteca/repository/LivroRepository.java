package br.edu.univassouras.biblioteca.repository;

import br.edu.univassouras.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acesso a dados dos livros. O Spring Data JPA gera a implementação automaticamente.
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
}
