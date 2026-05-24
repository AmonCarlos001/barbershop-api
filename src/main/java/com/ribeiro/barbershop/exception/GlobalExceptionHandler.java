package com.ribeiro.barbershop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Interceptador global de exceções da API para padronização e tratamento de respostas HTTP de erro. */

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Erros de IDs ou recurso não encontrado
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex){
        return construirRespostaErro(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    // Erro de digitação do JSON (esqueceu a vírgula, aspas ou digitação de formato)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
        return construirRespostaErro(
                HttpStatus.BAD_REQUEST,
                "Erro de digitação no JSON",
                "O corpo da requisição contém erros de sintaxe ou formatos inválidos. Verifique as aspas, vírgulas ou tipos de dados."
        );
    }

    //Erros de validação de campos (Ex: @NotBlank, @NotNull no DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensagensErros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        return construirRespostaErro(HttpStatus.BAD_REQUEST, "Dados inválidos", mensagensErros);
    }

    //Erro de email duplicado
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex){
        String mensagemEmailDuplicado = "Este e-mail já está cadastrado no sistema.";
        if (ex.getRootCause() != null && !ex.getRootCause().getMessage().contains("email")){
            mensagemEmailDuplicado = "Alguns dos dados informados já existem no nosso registro.";
        }
        return construirRespostaErro(HttpStatus.CONFLICT, "Dados já cadastrados", mensagemEmailDuplicado);
    }

    //Erros de digitação de tipo na URL (Ex:/barbeiros/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagem = String.format("O parâmetro '%s' recebeu o valor '%s' que é inválido. Esperava-se o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        return construirRespostaErro(HttpStatus.BAD_REQUEST, "Parâmetro inválido na URL", mensagem);
    }

    //Erro preventivo para qualquer incidente no sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return construirRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor",
                "Ocorreu um erro inesperado no sistema. Por favor, tente novamente mais tarde.");
    }

    //Evitar choque de agenda (Ex: Dois clientes agendamentos na mesma hora com o mesmo barbeiro)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return construirRespostaErro(HttpStatus.BAD_REQUEST, "Não foi possível agendar", ex.getMessage());
    }

    //Metodo para padronizar o formato JSON de resposta
    private ResponseEntity<Map<String, Object>> construirRespostaErro(HttpStatus status, String erro, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", erro);
        body.put("message", mensagem);
        return new ResponseEntity<>(body, status);
    }


}
