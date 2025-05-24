package com.project.recipes.controller.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.recipes.exceptions.ExceptionErrorPage;
import com.project.recipes.exceptions.ExceptionInvalidRecipeData;
import com.project.recipes.exceptions.ExceptionNotFoundRecipe;
import com.project.recipes.exceptions.ExceptionPageNotFound;
import com.project.recipes.persistance.models.ApiError;

@RestControllerAdvice
public class GlobalControllerError {
	
	//Maneja la excepción cuando los datos de la receta no son válidos
	 @ExceptionHandler(ExceptionInvalidRecipeData.class)
	 public ResponseEntity<ApiError> invalidRecipeData(ExceptionInvalidRecipeData e) {
	     ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
	     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	 
	// Maneja la excepción de error en la página
	@ExceptionHandler(ExceptionErrorPage.class)
	public ResponseEntity<ApiError> handleErrorPageException(ExceptionErrorPage e) {
	    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	// Maneja la excepción cuando una página no es encontrada
	@ExceptionHandler(ExceptionPageNotFound.class)
	public ResponseEntity<ApiError> pageNotFound(ExceptionPageNotFound e) {
		 ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	// Maneja la excepción cuando no se encuentra una receta
	 @ExceptionHandler(ExceptionNotFoundRecipe.class)
	 public ResponseEntity<ApiError> commentNotFoundException(ExceptionNotFoundRecipe e) {
	      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
	      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	 }

}
