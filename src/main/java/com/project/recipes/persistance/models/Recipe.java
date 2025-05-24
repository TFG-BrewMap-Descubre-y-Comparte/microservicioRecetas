package com.project.recipes.persistance.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "recipe")
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recetaID")
	private Integer recipeId;

	@Column(name = "id_user")
	private Integer userId;

	@Column(name = "titulo")
	private String tittle;

	@Column(name = "descripcion")
	private String description;

	@Column(name = "metodo")
	private String metodo;

	private String imagen;
	
	@Column(name = "origen")
	private String origen;


}
