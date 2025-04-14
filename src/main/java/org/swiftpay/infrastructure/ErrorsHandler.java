package org.swiftpay.infrastructure;

import br.com.caelum.stella.validation.InvalidStateException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.swiftpay.dtos.ErrorDTO;
import org.swiftpay.exceptions.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler({

            UserNotFoundException.class,

            UsernameNotFoundException.class

    })
    public ResponseEntity <ErrorDTO> handleUserNotFoundException(UserNotFoundException ex) {

        ErrorDTO response = new ErrorDTO(

                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <ErrorDTO> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();

        assert fieldError != null;

        ErrorDTO response = new ErrorDTO(

                HttpStatus.BAD_REQUEST.value(),
                fieldError.getDefaultMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({

            InvalidStateException.class,
            BadRequestException.class,
            NonActiveUserException.class,
            AlreadyActiveException.class,
            InvalidCPFException.class,
            InvalidCNPJException.class,
            SelfTransferException.class,
            InvalidAmountException.class,
            InvalidTypeOfPayerException.class,

    })
    public ResponseEntity <ErrorDTO> handleBadRequest (Exception ex) {

        ErrorDTO response = new ErrorDTO(

                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({

            RuntimeException.class,

            Exception.class

    })
    public ResponseEntity <ErrorDTO> handleExceptions(Exception ex) {

        ErrorDTO errorMessage = new ErrorDTO(

                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity <ErrorDTO> handleForbiddenAccessException(ForbiddenAccessException ex) {

        ErrorDTO response = new ErrorDTO(

                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(APIErrorException.class)
    public ResponseEntity <ErrorDTO> handleServiceUnavailable (APIErrorException ex) {

        ErrorDTO response = new ErrorDTO(

                HttpStatus.GATEWAY_TIMEOUT.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);

    }

}
