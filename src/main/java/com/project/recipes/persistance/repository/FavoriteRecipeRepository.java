package com.project.recipes.persistance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.recipes.persistance.models.FavoriteRecipe;

public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Integer> {
	
    Optional<FavoriteRecipe> findByUserIdAndRecipeId(Integer userId, Integer recipeId);
    List<FavoriteRecipe> findByUserId(Integer userId);
    void deleteByUserIdAndRecipeId(Integer userId, Integer recipeId);
}