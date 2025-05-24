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

import com.project.recipes.dto.RecipeDTO;
import com.project.recipes.exceptions.ExceptionInvalidRecipeData;
import com.project.recipes.exceptions.ExceptionNotFoundRecipe;
import com.project.recipes.persistance.models.JwtUtil;
import com.project.recipes.persistance.models.Recipe;
import com.project.recipes.persistance.models.Response;
import com.project.recipes.persistance.repository.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeServiceI{
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private JwtUtil jwtUtils;


	@Override
	public ResponseEntity<Response<RecipeDTO>> addRecipe(RecipeDTO dto,  String authHeader) {
	    try {
	        // Validaciones
	        if (dto.getTittle() == null || dto.getTittle().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The title cannot be empty");
	        }
	        if (dto.getMetodo() == null || dto.getMetodo().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The method cannot be empty");
	        }
	        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The description cannot be empty");
	        }
	        if (dto.getOrigen() == null || dto.getOrigen().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The origin cannot be empty");
	        }

	        // Extraer token del header
	        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
	        System.out.println("TOKEN RECIBIDO: " + token);
	        Integer userId = jwtUtils.getUserIdFromToken(token);
	        System.out.println("USER ID EXTRAÍDO DEL TOKEN: " + userId);

	        if (userId == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new Response<>(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
	        }

	        // Convertir DTO a entidad
	        Recipe recipe = new Recipe();
	        recipe.setUserId(userId);
	        recipe.setTittle(dto.getTittle());
	        recipe.setDescription(dto.getDescription());
	        recipe.setMetodo(dto.getMetodo());
	        recipe.setOrigen(dto.getOrigen());

	        // Imagen por defecto
	        recipe.setImagen("/assets/imagenesRecetas/sibarist20223481_1200x1200.png");

	        // Guardar en base de datos
	        recipeRepository.save(recipe);

	        // Convertir entidad guardada a DTO
	        RecipeDTO responseDTO = new RecipeDTO();
	        responseDTO.setId(recipe.getRecipeId());
	        responseDTO.setUserId(recipe.getUserId());
	        responseDTO.setTittle(recipe.getTittle());
	        responseDTO.setDescription(recipe.getDescription());
	        responseDTO.setMetodo(recipe.getMetodo());
	        responseDTO.setOrigen(recipe.getOrigen());
	        responseDTO.setImagen(recipe.getImagen());

	        // Devolver respuesta
	        Response<RecipeDTO> response = new Response<>(HttpStatus.CREATED, "Recipe created successfully");
	        return ResponseEntity.status(HttpStatus.OK).body(response);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred: " + e.getMessage()));
	    }
	}


	@Override
	public Page<RecipeDTO> getAllRecipesPaginated(Pageable pageable) {
		
		Page<Recipe> recipesPage = recipeRepository.findAll(pageable);
		
		List<RecipeDTO> recipesDTOs = new ArrayList<>();
		for(Recipe recipe : recipesPage) {
			RecipeDTO recipeDTO = new RecipeDTO(recipe);
			recipesDTOs.add(recipeDTO);
		}
		
		return new PageImpl<>(recipesDTOs, pageable, recipesPage.getTotalElements());
	}


	@Override
	public ResponseEntity<Response<Recipe>> updateRecipe(int id, Recipe updateRecipe, String authHeader) {
	    Optional<Recipe> recipeOptional = recipeRepository.findByRecipeId(id);

	    if (recipeOptional.isEmpty()) {
	        throw new ExceptionNotFoundRecipe("Recipe not found");
	    }

	    try {
	        // Extraer y validar el token
	        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
	        Integer userIdFromToken = jwtUtils.getUserIdFromToken(token);

	        if (userIdFromToken == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new Response<>(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
	        }

	        Recipe existingRecipe = recipeOptional.get();

	        // Verificar que el usuario logueado es el dueño de la receta
	        if (!existingRecipe.getUserId().equals(userIdFromToken)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body(new Response<>(HttpStatus.FORBIDDEN, "You are not authorized to update this recipe"));
	        }

	        // Validar los nuevos datos
	        if (updateRecipe.getTittle() == null || updateRecipe.getTittle().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The title cannot be empty");
	        }
	        if (updateRecipe.getMetodo() == null || updateRecipe.getMetodo().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The method cannot be empty");
	        }
	        if (updateRecipe.getDescription() == null || updateRecipe.getDescription().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The description cannot be empty");
	        }
	        if (updateRecipe.getOrigen() == null || updateRecipe.getOrigen().trim().isEmpty()) {
	            throw new ExceptionInvalidRecipeData("The origin cannot be empty");
	        }

	        // Actualizar la receta
	        existingRecipe.setTittle(updateRecipe.getTittle());
	        existingRecipe.setMetodo(updateRecipe.getMetodo());
	        existingRecipe.setDescription(updateRecipe.getDescription());
	        existingRecipe.setOrigen(updateRecipe.getOrigen());

	       
	        if (updateRecipe.getImagen() != null && !updateRecipe.getImagen().trim().isEmpty()) {
	            existingRecipe.setImagen(updateRecipe.getImagen());
	        }

	        recipeRepository.save(existingRecipe);

	        Response<Recipe> response = new Response<>(HttpStatus.OK, "Recipe updated successfully");
	        return ResponseEntity.status(HttpStatus.OK).body(response);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage()));
	    }
	}


	@Override
	public ResponseEntity<Response<Recipe>> deleteRecipe(int id, String authHeader) {
		
		Optional<Recipe> recipeOptional = recipeRepository.findByRecipeId(id);

	    if (recipeOptional.isEmpty()) {
	        throw new ExceptionNotFoundRecipe("Recipe not found");
	    }
	    
	    try {
	    	
	    	// Extraer y validar el token
	        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
	        Integer userIdFromToken = jwtUtils.getUserIdFromToken(token);

	        if (userIdFromToken == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new Response<>(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
	        }

	        Recipe existingRecipe = recipeOptional.get();

	        // Verificar que el usuario logueado es el dueño de la receta
	        if (!existingRecipe.getUserId().equals(userIdFromToken)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body(new Response<>(HttpStatus.FORBIDDEN, "You are not authorized to update this recipe"));
	        }
	        
	        recipeRepository.delete(existingRecipe);

	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new Response<>(HttpStatus.OK, "Recipe deleted successfully"));
	    	
	    }catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage()));
	    }

	}


	@Override
	public RecipeDTO getRecipeById(int id) {
	    Optional<Recipe> recipeOptional = recipeRepository.findByRecipeId(id);

	    if (recipeOptional.isEmpty()) {
	        throw new ExceptionNotFoundRecipe("Recipe not found");
	    }

	    Recipe existingRecipe = recipeOptional.get();

	    // Usas el constructor del DTO
	    return new RecipeDTO(existingRecipe);
	}






}
