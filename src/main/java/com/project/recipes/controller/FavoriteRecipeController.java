	package com.project.recipes.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.exceptions.ExceptionErrorPage;
import com.project.recipes.exceptions.ExceptionPageNotFound;
import com.project.recipes.persistance.models.FavoriteRecipe;
import com.project.recipes.persistance.models.Response;
import com.project.recipes.services.FavoriteRecipeServiceI;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteRecipeController {
	
	@Autowired
    private FavoriteRecipeServiceI favoriteService;
	
	// Añadir una receta a favoritos
	@PostMapping("/{recipeId}")
    public ResponseEntity<Response<FavoriteRecipe>> addFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable("recipeId") Integer recipeId) {
        return favoriteService.addFavorite(token, recipeId);
    }
	
	// Eliminar una receta de favoritos
	@DeleteMapping("/favorites/{recipeId}")
    public ResponseEntity<Response<FavoriteRecipe>> removeFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable("recipeId") Integer recipeId) {
        return favoriteService.removeFavorite(token, recipeId);
    }
    
    // Obtener todas las recetas favoritas del usuario
    @GetMapping("favouriteRecipes")
    public ResponseEntity<Page<RecipeDTO>> getUserFavorites(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestHeader("Authorization") String token) {

        // Validación de la paginación
        if (page < 0) {
            throw new ExceptionErrorPage("Page index must not be less than zero.");
        }
        if (size <= 0) {
            throw new ExceptionErrorPage("Page size must be greater than zero.");
        }

        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<RecipeDTO> recipes = favoriteService.getUserFavoritesPaginated(token, pageable);
            if (recipes.isEmpty() && page > 0) {
                throw new ExceptionPageNotFound("The page index is out of range.");
            }
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionPageNotFound("Error real: " + e.getMessage());
        }
    }
    
    @GetMapping("/favorites")
    public ResponseEntity<List<RecipeDTO>> getUserFavorites(@RequestHeader("Authorization") String token) {
        return favoriteService.getFavoritesByUserId(token);
    }



}
