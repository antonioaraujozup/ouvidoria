package br.com.zup.edu.ouvidoria.api.reclamacao;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_RECLAMACAO_TEXTO_HASH_CELULAR", columnNames = {"texto", "hash_do_celular"})
})
@Entity
public class Reclamacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario", nullable = false)
    private String nome;

    @Column(name = "email_usuario", nullable = false)
    private String email;

    @Embedded
    private Celular celular;

    @Lob
    @Column(nullable = false, length = 4000)
    private String texto;

    @Column(nullable = false)
    private LocalDateTime registradoEm = LocalDateTime.now();

    public Reclamacao(String nome, String email, String celular, String texto) {
        this.nome = nome;
        this.email = email;
        this.celular = new Celular(celular);
        this.texto = texto;
    }

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Reclamacao() {
    }

    public Long getId() {
        return id;
    }
}
