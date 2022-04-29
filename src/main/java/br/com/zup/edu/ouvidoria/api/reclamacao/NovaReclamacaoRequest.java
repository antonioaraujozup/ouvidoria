package br.com.zup.edu.ouvidoria.api.reclamacao;

import javax.validation.constraints.*;

public class NovaReclamacaoRequest {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^\\+[1-9][0-9]\\d{1,14}")
    private String celular;

    @NotBlank
    @Size(max = 4000)
    private String texto;

    public NovaReclamacaoRequest(String nome, String email, String celular, String texto) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.texto = texto;
    }

    public NovaReclamacaoRequest() {
    }

    public Reclamacao toModel() {
        return new Reclamacao(nome,email,celular,texto);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public String getTexto() {
        return texto;
    }
}
