package br.com.zup.pagamentos.compartilhado.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ExceptionsHandler {

    private final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class.getSimpleName());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionHandlerResponse methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex) {
        logger.error("methodArgumentNotValidExceptionHandler: " + ex.getMessage());

        var map = new HashMap<String, List<String>>();

        ex.getBindingResult().getFieldErrors().forEach(erro -> {
            if (map.containsKey(erro.getField())) {
                map.get(erro.getField()).add(erro.getDefaultMessage());
            } else {
                var list = new ArrayList<String>();
                list.add(erro.getDefaultMessage());
                map.put(erro.getField(), list);
            }
        });

        return new ExceptionHandlerResponse(map, LocalDateTime.now());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionHandlerResponse> responseStatusExceptionHandler(ResponseStatusException ex) {
        logger.error("responseStatusExceptionHandler: " + ex.getMessage());

        var map = new HashMap<String, List<String>>();
        map.put("mensagem", List.of(Objects.requireNonNull(ex.getReason())));

        var response = new ExceptionHandlerResponse(map, LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionHandlerResponse exceptionHandler(Exception ex) {
        logger.error("exceptionHandler: " + ex.getMessage());

        var map = new HashMap<String, List<String>>();
        map.put("erro", List.of("Desculpe, um erro inesperado ocorreu"));

        return new ExceptionHandlerResponse(map, LocalDateTime.now());
    }


}
