package com.project.recipes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.persistance.models.FavoriteRecipe;
import com.project.recipes.persistance.models.JwtUtil;
import com.project.recipes.persistance.models.Recipe;
import com.project.recipes.persistance.models.Response;
import com.project.recipes.persistance.repository.FavoriteRecipeRepository;
import com.project.recipes.persistance.repository.RecipeRepository;

@Service
public class FavoriteRecipeServiceImpl implements FavoriteRecipeServiceI{
	
	@Autowired
    private FavoriteRecipeRepository favoriteRepo;

    @Autowired
    private RecipeRepository recipeRepository;
	
	@Autowired
    private JwtUtil jwtUtil;

	@Override
	public ResponseEntity<Response<FavoriteRecipe>> addFavorite(String tokenHeader, Integer recipeId) {
		
		String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
		Integer userId = jwtUtil.getUserIdFromToken(token);
		
		FavoriteRecipe favorite = new FavoriteRecipe();
        favorite.setUserId(userId);
        favorite.setRecipeId(recipeId);
        favoriteRepo.save(favorite);

        Response<FavoriteRecipe> response = new Response<>(HttpStatus.CREATED, "Favorite recipe added");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

	@Override
	@Transactional
	public ResponseEntity<Response<FavoriteRecipe>> removeFavorite(String tokenHeader, Integer recipeId) {
	    try {
	        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
	        Integer userId = jwtUtil.getUserIdFromToken(token);

	        if (userId == null) {
	            Response<FavoriteRecipe> response = new Response<>(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }

	        Optional<FavoriteRecipe> favoriteOptional = favoriteRepo.findByUserIdAndRecipeId(userId, recipeId);

	        if (favoriteOptional.isPresent()) {
	            favoriteRepo.deleteByUserIdAndRecipeId(userId, recipeId);
	            Response<FavoriteRecipe> response = new Response<>(HttpStatus.OK, "Receta favorita eliminada correctamente");
	            return ResponseEntity.ok(response);
	        } else {
	            Response<FavoriteRecipe> response = new Response<>(HttpStatus.NOT_FOUND, "La receta no se encuentra en favoritos");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); // o usa log.error("Error eliminando favorito", e);
	        Response<FavoriteRecipe> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error eliminando la receta favorita");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}




	@Override
	public Page<RecipeDTO> getUserFavoritesPaginated(String tokenHeader, Pageable pageable) {
	    String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
	    Integer userId = jwtUtil.getUserIdFromToken(token);

	    // Obtener todas las recetas favoritas del usuario
	    List<FavoriteRecipe> favoriteList = favoriteRepo.findByUserId(userId);

	    // Extraer los IDs de las recetas favoritas
	    List<Integer> favoriteRecipeIds = new ArrayList<>();
	    for (FavoriteRecipe fav : favoriteList) {
	        favoriteRecipeIds.add(fav.getRecipeId());
	    }

	    // Calcular índices de la página actual
	    int start = (int) pageable.getOffset();
	    int end = Math.min(start + pageable.getPageSize(), favoriteRecipeIds.size());

	    // Obtener los IDs paginados
	    List<Integer> paginatedIds = new ArrayList<>();
	    for (int i = start; i < end; i++) {
	        paginatedIds.add(favoriteRecipeIds.get(i));
	    }

	    // Buscar las recetas y convertir a DTO
	    List<RecipeDTO> favoriteRecipes = new ArrayList<>();
	    for (Integer recipeId : paginatedIds) {
	        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
	        if (recipeOpt.isPresent()) {
	            Recipe recipe = recipeOpt.get();
	            RecipeDTO dto = new RecipeDTO(recipe);
	            favoriteRecipes.add(dto);
	        }
	    }

	    return new PageImpl<>(favoriteRecipes, pageable, favoriteRecipeIds.size());
	}

	 
	 @Override
	    public List<FavoriteRecipe> findAllByUserId(Integer userId) {
	        return favoriteRepo.findByUserId(userId);
	 }
	 
	 @Override
	 public ResponseEntity<List<RecipeDTO>> getFavoritesByUserId(String tokenHeader) {
	     String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
	     Integer userId = jwtUtil.getUserIdFromToken(token);

	     List<FavoriteRecipe> favorites = favoriteRepo.findByUserId(userId);
	     List<RecipeDTO> favoriteDTOs = new ArrayList<>();

	     for (int i = 0; i < favorites.size(); i++) {
	         FavoriteRecipe fav = favorites.get(i);
	         Optional<Recipe> recipeOpt = recipeRepository.findById(fav.getRecipeId());
	         if (recipeOpt.isPresent()) {
	             Recipe recipe = recipeOpt.get();
	             RecipeDTO dto = new RecipeDTO(recipe);
	             favoriteDTOs.add(dto);
	         }
	     }

	     return ResponseEntity.ok(favoriteDTOs);
	 }




}
