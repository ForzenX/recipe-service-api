package recipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RecipeDto;
import recipes.dto.RecipeIdDto;
import recipes.entity.RecipeEntity;
import recipes.entity.UserEntity;
import recipes.mapper.RecipeMapper;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final UserRepository userRepository;

    public ResponseEntity<RecipeIdDto> postRecipe(RecipeDto recipeDto, UserEntity userEntity) {
        RecipeEntity recipeEntity = recipeMapper.mapDtoToEntity(recipeDto);

        recipeEntity.setUser(userEntity);
        recipeRepository.save(recipeEntity);

        return ResponseEntity.ok(new RecipeIdDto(recipeEntity.getId()));
    }

    public ResponseEntity<RecipeDto> getRecipe(Long id) {
        RecipeEntity recipe = recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with this id doesn't exist"));

        return ResponseEntity.ok(recipeMapper.mapEntityToDto(recipe));
    }

    public ResponseEntity<Object> deleteRecipe(Long id, UserDetails userDetails) {
        RecipeEntity recipeEntity = recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        UserEntity userEntity = userRepository.findUserEntityByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        if (userEntity.getRecipes().contains(recipeEntity)) {
            userEntity.getRecipes().remove(recipeEntity);
            userRepository.save(userEntity);
            return ResponseEntity.noContent().build();
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not delete this recipe!");
    }

    public ResponseEntity<Object> updateRecipe(Long id, RecipeDto recipeDto, UserDetails userDetails) {
        RecipeEntity recipeEntity = recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipe with id %d not found".formatted(id)))
                .getInfoFromDto(recipeDto);

        UserEntity userEntity = userRepository.findUserEntityByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        if (recipeEntity.getUser() == userEntity) {
            recipeRepository.save(recipeEntity);
            userEntity.getRecipes().add(recipeEntity);
            userRepository.save(userEntity);
            return ResponseEntity.noContent().build();
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not update this recipe!");
    }

    public ResponseEntity<List<RecipeDto>> searchRecipes(String category, String name) {
        if ((Objects.nonNull(category) && Objects.nonNull(name))
                || (!Objects.nonNull(category) && !Objects.nonNull(name))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter correct category or name");
        }

        List<RecipeEntity> recipeEntities = Objects.nonNull(category)
                ? recipeRepository.findByCategoryIgnoreCaseOrderByTimeOfPublishingDesc(category)
                : recipeRepository.findAllByNameContainingIgnoreCaseOrderByTimeOfPublishingDesc(name);

        return ResponseEntity.ok(
                recipeEntities
                        .stream()
                        .map(recipeMapper::mapEntityToDto)
                        .toList()
        );
    }

}
