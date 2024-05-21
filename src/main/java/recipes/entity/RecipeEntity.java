package recipes.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import recipes.dto.RecipeDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @UpdateTimestamp
    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeOfPublishing;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_directions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "direction")
    private List<String>  directions;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user;

    public RecipeEntity getInfoFromDto(RecipeDto recipeDto) {
        this.name = recipeDto.getName();
        this.category = recipeDto.getCategory();
        this.description = recipeDto.getDescription();
        this.ingredients = recipeDto.getIngredients();
        this.directions = recipeDto.getDirections();
        return this;
    }

}
