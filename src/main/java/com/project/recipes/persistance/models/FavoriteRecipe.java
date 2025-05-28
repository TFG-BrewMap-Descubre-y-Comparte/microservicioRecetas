package com.project.recipes.persistance.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "favorite_recipes", uniqueConstraints = @UniqueConstraint(columnNames = {"id_user", "id_recipe"}))
public class FavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorite")
    private Integer idFavorite;

    @Column(name = "id_user", nullable = false)
    private Integer userId;

    @Column(name = "id_recipe", nullable = false)
    private Integer recipeId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
