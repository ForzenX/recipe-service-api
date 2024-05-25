package recipes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDto;
import recipes.dto.RecipeIdDto;
import recipes.dto.RegisterInfoDto;
import recipes.service.RecipeService;
import recipes.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    private final UserService userService;

    @PostMapping("/recipe/new")
    public ResponseEntity<RecipeIdDto> postRecipe(
            @Valid @RequestBody RecipeDto recipeDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.postRecipe(recipeDto, userDetails);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Object> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.deleteRecipe(id, userDetails);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Object> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeDto recipeDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.updateRecipe(id, recipeDto, userDetails);
    }

    @GetMapping("/recipe/search/")
    public ResponseEntity<List<RecipeDto>> searchRecipes(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "name", required = false) String name
    ) {
        return recipeService.searchRecipes(category, name);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterInfoDto> registerUser(@Valid @RequestBody RegisterInfoDto registerInfoDto) {
        return userService.registerUser(registerInfoDto);
    }
}
