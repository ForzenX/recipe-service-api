package recipes.handler;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import recipes.dto.ErrorDto;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        ErrorDto errorDto = ErrorDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .error("Bad request")
                .message(authException.getMessage())
                .path(request.getRequestURI())
                .build();

        response.setStatus(HttpStatus.BAD_REQUEST.value());

        try (PrintWriter writer = response.getWriter()) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());

            writer.print(jsonMapper.writeValueAsString(errorDto));
        }
    }
}
