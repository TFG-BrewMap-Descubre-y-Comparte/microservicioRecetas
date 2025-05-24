package com.project.recipes.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.recipes.persistance.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	
	Optional<Recipe> findByRecipeId (int recipeID);

}
