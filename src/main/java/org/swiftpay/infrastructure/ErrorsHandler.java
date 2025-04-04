package org.swiftpay.infrastructure;

import br.com.caelum.stella.validation.InvalidStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.swiftpay.dtos.ErrorDTO;
import org.swiftpay.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ErrorsHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorDTO handleNotFoundException (UserNotFoundException ex) {

        return new ErrorDTO(ex.getMessage());

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidStateException.class)
    public ErrorDTO handleInvalidCPFException (InvalidStateException ex) {

        return new ErrorDTO(ex.getMessage());

    }

}
