package br.edu.univassouras.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidade que representa um livro cadastrado na biblioteca.
 */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "O autor é obrigatório")
    @Column(nullable = false)
    private String autor;

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

    @NotNull(message = "O ano de publicação é obrigatório")
    @Min(value = 1000, message = "Ano de publicação inválido")
    @Max(value = 2100, message = "Ano de publicação inválido")
    private Integer anoPublicacao;

    @NotNull(message = "O status de leitura é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusLeitura status;

    public Livro() {
    }

    public Livro(String titulo, String autor, String genero, Integer anoPublicacao, StatusLeitura status) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.status = status;
    }

    /** Copia os dados de outro livro para este (usado na atualização). */
    public void atualizarDados(Livro dados) {
        this.titulo = dados.getTitulo();
        this.autor = dados.getAutor();
        this.genero = dados.getGenero();
        this.anoPublicacao = dados.getAnoPublicacao();
        this.status = dados.getStatus();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public StatusLeitura getStatus() { return status; }
    public void setStatus(StatusLeitura status) { this.status = status; }
}
