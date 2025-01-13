package com.rocketseat.gestao_vagas.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // cria um construtor com argumentos
public class ErrorMessageDTO {
    // arquivo para ser o Data Transfer foir Object -> converte um objeto para o
    // formato que está sendo passado

    private String message;
    private String field;

}
