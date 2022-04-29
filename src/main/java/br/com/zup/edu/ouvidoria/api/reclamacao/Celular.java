package br.com.zup.edu.ouvidoria.api.reclamacao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Embeddable
public class Celular {

    @Column(name = "numero_do_celular", nullable = false)
    private String numero;

    @Column(name = "hash_do_celular", nullable = false, length = 64)
    private String hashDoNumero;

    public Celular(String numero) {
        this.numero = this.anonymize(numero);
        this.hashDoNumero = this.hash(numero);
    }

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Celular() {
    }

    private String anonymize(String celular) {
        String masked = "+## (##) #####-####";
        return masked;
    }

    private String hash(String celular) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hash = digest.digest(celular.getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException("Erro ao gerar hash do telefone celular: " + celular);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    public String getHashDoNumero() {
        return hashDoNumero;
    }
}
