package com.project.recipes.persistance.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.recipes.persistance.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	
	Optional<Recipe> findByRecipeId (int recipeID);
	
	 Page<Recipe> findByMetodoIgnoreCase(String metodo, Pageable pageable);

}
