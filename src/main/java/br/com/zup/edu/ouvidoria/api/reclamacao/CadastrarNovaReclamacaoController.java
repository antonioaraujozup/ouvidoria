package br.com.zup.edu.ouvidoria.api.reclamacao;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CadastrarNovaReclamacaoController {

    private final ReclamacaoRepository repository;

    public CadastrarNovaReclamacaoController(ReclamacaoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/reclamacoes")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid NovaReclamacaoRequest request, UriComponentsBuilder uriComponentsBuilder) {

        Celular celular = new Celular(request.getCelular());
        if (repository.existsByTextoAndCelularHashDoNumero(request.getTexto(), celular.getHashDoNumero())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Reclamação já cadastrada");
        }

        Reclamacao reclamacao = request.toModel();

        repository.save(reclamacao);

        URI location = uriComponentsBuilder.path("/reclamacoes/{id}")
                .buildAndExpand(reclamacao.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> body = Map.of(
                "status", 422,
                "error", "Unprocessable Entity",
                "path", request.getDescription(false).replace("uri=", ""),
                "timestamp", LocalDateTime.now(),
                "message", "Reclamação já cadastrada"
        );

        return ResponseEntity.unprocessableEntity().body(body);
    }
}
