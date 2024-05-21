package recipes.handler;


import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import recipes.dto.ErrorDto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class DataValidationHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders httpHeaders,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError fieldError) {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                });

        String errorMessage = errors.entrySet().stream()
                .map(e -> e.getKey().concat(": ").concat(e.getValue()))
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(
                ErrorDto.builder()
                        .httpStatus(statusCode.value())
                        .error("Bad Request")
                        .message(errorMessage)
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(
            @Nullable TypeMismatchException ex,
            @Nullable HttpHeaders headers,
            @Nullable HttpStatusCode status,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().body(
                ErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad request")
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
                .forEach( constraintViolation ->
                        errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())
                );

        return ResponseEntity.badRequest().body(
                ErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad Request")
                        .message(errors.entrySet().stream()
                                .map(e -> e.getKey() + ": " + e.getValue())
                                .collect(Collectors.joining("; ")))
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().body(
                ErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }
}
