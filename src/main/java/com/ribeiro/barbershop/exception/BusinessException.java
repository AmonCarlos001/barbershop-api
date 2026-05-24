package com.ribeiro.barbershop.exception;

/** Exceção para encapsular e sinalizar violações de regras de negócio da API. */

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
