package com.project.recipes.dto;

import com.project.recipes.persistance.models.Recipe;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecipeDTO {
    
    private Integer id;
    private Integer userId;
    private String tittle;
    private String description;
    private String metodo;
    private String imagen;
    private String origen;
    
    public RecipeDTO (Recipe recipe) {
    	this.id = recipe.getRecipeId();
    	this.userId = recipe.getUserId();
    	this.tittle = recipe.getTittle();
    	this.description = recipe.getDescription();
    	this.metodo = recipe.getMetodo();
    	this.imagen = recipe.getImagen();
    	this.origen = recipe.getOrigen();
    }
}
