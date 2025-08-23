package com.lucasgalliani.credit_card_payments.infra;

import com.lucasgalliani.credit_card_payments.dto.ExceptionDto;
import com.lucasgalliani.credit_card_payments.infra.exception.EntityNotFoundException;
import com.lucasgalliani.credit_card_payments.infra.exception.TransacaoNaoAutorizadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity notFoundEntity(EntityNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto("Transação não encontrada!", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }

    @ExceptionHandler(TransacaoNaoAutorizadaException.class)
    public ResponseEntity unauthorizedTransaction(TransacaoNaoAutorizadaException ex) {
        ExceptionDto exceptionDto = new ExceptionDto("Só é possível estornar transações autorizadas!", "400");
        return ResponseEntity.badRequest().body(exceptionDto);
    }
}
