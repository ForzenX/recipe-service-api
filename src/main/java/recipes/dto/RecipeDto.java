package recipes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {

    @NotBlank(message = "name of recipe is required")
    private String name;

    @NotBlank(message = "category of recipe is required")
    private String category;

    @NotBlank(message = "description of recipe is required")
    private String description;

    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeOfPublishing = LocalDateTime.now();

    @NotNull(message = "ingredients is required")
    @Size(min = 1, message = "Recipe must contain at least 1 ingredient")
    private List<String> ingredients;

    @NotNull(message = "directions is required")
    @Size(min = 1, message = "Recipe must contain at least 1 direction")
    private List<String>  directions;

}
