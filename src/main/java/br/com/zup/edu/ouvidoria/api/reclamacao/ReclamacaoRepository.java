package br.com.zup.edu.ouvidoria.api.reclamacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamacaoRepository extends JpaRepository<Reclamacao,Long> {
    boolean existsByTextoAndCelularHashDoNumero(String texto, String hashDoNumero);
}
