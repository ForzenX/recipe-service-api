package recipes.mapper;

import org.springframework.stereotype.Component;
import recipes.dto.RecipeDto;
import recipes.entity.RecipeEntity;

@Component
public class RecipeMapper {

    public RecipeDto mapEntityToDto(RecipeEntity recipeEntity) {
        return RecipeDto
                .builder()
                .name(recipeEntity.getName())
                .category(recipeEntity.getCategory())
                .description(recipeEntity.getDescription())
                .timeOfPublishing(recipeEntity.getTimeOfPublishing())
                .ingredients(recipeEntity.getIngredients())
                .directions(recipeEntity.getDirections())
                .build();
    }

    public RecipeEntity mapDtoToEntity(RecipeDto recipeDto) {
        return RecipeEntity
                .builder()
                .name(recipeDto.getName())
                .category(recipeDto.getCategory())
                .description(recipeDto.getDescription())
                .timeOfPublishing(recipeDto.getTimeOfPublishing())
                .ingredients(recipeDto.getIngredients())
                .directions(recipeDto.getDirections())
                .build();
    }

}
