package gible.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handle(CustomException ex){
        ErrorType errortype = ex.getErrorCode();
        log.warn("Error occurred : [errorCode={}, message={}]", errortype.getStatus(), errortype.getMessage());
        return ResponseEntity.status(errortype.getStatus()).body(ErrorDto.of(errortype.getStatus(), errortype.getMessage()));
    }
}
