package com.project.recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.exceptions.ExceptionErrorPage;
import com.project.recipes.exceptions.ExceptionPageNotFound;
import com.project.recipes.persistance.models.Recipe;
import com.project.recipes.persistance.models.Response;
import com.project.recipes.services.RecipeServiceI;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class RecipeController {
	
		@Autowired
		private RecipeServiceI recipeService;
		
		@PostMapping("/recipe")
		public ResponseEntity<Response<RecipeDTO>> addRecipe(
		        @RequestBody RecipeDTO dto,
		        @RequestHeader("Authorization") String authHeader) {
		    return recipeService.addRecipe(dto, authHeader);
		}
		
		@GetMapping("/recipes")
		public ResponseEntity<Page<RecipeDTO>> getAllRecipes(
		        @RequestParam(name = "page", defaultValue = "0") int page,
		        @RequestParam(name = "size", defaultValue = "5") int size) {
	
		    // Validación de la paginación
		    if (page < 0) {
		        throw new ExceptionErrorPage("Page index must not be less than zero.");
		    }
		    if (size <= 0) {
		        throw new ExceptionErrorPage("Page size must be greater than zero.");
		    }
	
		    Pageable pageable = PageRequest.of(page, size);
	
		    try {
		        Page<RecipeDTO> recipes = recipeService.getAllRecipesPaginated(pageable);
		        if (recipes.isEmpty() && page > 0) {
		            throw new ExceptionPageNotFound("The page index is out of range.");
		        }
		        return ResponseEntity.ok(recipes);
		    } catch (Exception e) {
		        throw new ExceptionPageNotFound("The page index is out of range.");
		    }
		}
		
		@PutMapping("/recipe/{id}")
		public ResponseEntity<Response<Recipe>> updateRecipe(
				@PathVariable("id") int id,
		        @RequestBody Recipe recipe,
		        @RequestHeader("Authorization") String authHeader) {
		    return recipeService.updateRecipe(id, recipe, authHeader);
		}
		
		@DeleteMapping("/recipe/{id}")
		public ResponseEntity<Response<Recipe>> deleteRecipe(
		        @PathVariable("id") int id,
		        @RequestHeader("Authorization") String authHeader) {
		    return recipeService.deleteRecipe(id, authHeader);
		}
		
		@GetMapping("/recipe/{id}")
		public RecipeDTO getRecipeById(@PathVariable("id") int id) {
			return recipeService.getRecipeById(id);
		}





}