package com.project.recipes.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.persistance.models.FavoriteRecipe;
import com.project.recipes.persistance.models.Response;

public interface FavoriteRecipeServiceI {
	
	ResponseEntity<Response<FavoriteRecipe>> addFavorite(String tokenHeader, Integer recipeId);
	
	ResponseEntity<Response<FavoriteRecipe>>removeFavorite(String tokenHeader, Integer recipeId);
	 
	Page<RecipeDTO> getUserFavoritesPaginated(String tokenHeader, Pageable pageable);
	
	List<FavoriteRecipe> findAllByUserId(Integer userId);
	
	ResponseEntity<List<RecipeDTO>> getFavoritesByUserId(String token);



}
