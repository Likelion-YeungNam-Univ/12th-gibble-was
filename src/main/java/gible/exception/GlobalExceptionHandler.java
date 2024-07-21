package gible.exception;


import gible.exception.dto.ErrorRes;
import gible.exception.error.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static gible.exception.error.ErrorType.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handle(CustomException ex){
        ErrorType errortype = ex.getErrortype();
        log.error("Error occurred : [errorCode={}, message={}]", errortype.getStatus(), errortype.getMessage());
        return ResponseEntity.status(errortype.getStatus()).body(ErrorRes.of(errortype.getStatus(), errortype.getMessage()));
    }

    /* 일반 예외 처리 */
    @ExceptionHandler
    protected ResponseEntity customServerException(Exception ex) {
        ErrorRes error = new ErrorRes(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage());
        log.error("Error occurred : [errorCode={}, message={}]", error.status(), error.message());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.error("Error occurred : [message={}]", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
