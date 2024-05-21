package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.entity.RecipeEntity;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    List<RecipeEntity> findByCategoryIgnoreCaseOrderByTimeOfPublishingDesc(String category);
    List<RecipeEntity> findAllByNameContainingIgnoreCaseOrderByTimeOfPublishingDesc(String name);
}
