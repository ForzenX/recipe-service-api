package recipes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterInfoDto {

    @NotNull(message = "email can't be empty")
    @Pattern(regexp = "\\w+[.\\w+]*@\\w+\\.\\w+", message = "email should be valid")
    private String email;

    @NotBlank(message = "password can't be empty")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
}
