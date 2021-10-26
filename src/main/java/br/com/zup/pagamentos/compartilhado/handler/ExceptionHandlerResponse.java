package br.com.zup.pagamentos.compartilhado.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ExceptionHandlerResponse(Map<String, List<String>> erros, LocalDateTime ocorridoEm) {
}
