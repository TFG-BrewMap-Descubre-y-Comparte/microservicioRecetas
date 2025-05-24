package com.project.recipes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.persistance.models.Recipe;
import com.project.recipes.persistance.models.Response;

public interface RecipeServiceI {
	
		//Método para agregar una receta, devolviendo un ResponseEntity
		ResponseEntity<Response<RecipeDTO>> addRecipe (RecipeDTO dto, String authHeader);
		
		Page<RecipeDTO> getAllRecipesPaginated(Pageable pageable);
		
		//Método para editar una receta, devolviendo un ResponseEntity
		ResponseEntity<Response<Recipe>> updateRecipe(int id, Recipe updateRecipe, String authHeader);
		
		//Método para eliminar una receta, devolviendo un ResponseEntity
		ResponseEntity<Response<Recipe>> deleteRecipe(int id, String authHeader);
		
		RecipeDTO getRecipeById(int id);


}
