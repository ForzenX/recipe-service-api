package recipes.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.ErrorDto;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> responseStatusExceptionHandler(
            ResponseStatusException exception,
            HttpStatus statusCode,
            WebRequest request) {
        HttpStatusCode httpStatusCode = exception.getStatusCode();
        String errorMessage = exception.getMessage().substring(httpStatusCode.toString().length()).trim();
        return new ResponseEntity<>(
            ErrorDto.builder()
                    .httpStatus(statusCode.value())
                    .error(httpStatusCode.toString())
                    .message(errorMessage)
                    .path(request.getDescription(false).replace("uri=", ""))
                    .build(),
                httpStatusCode
        );
    }
}
