package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipeProcess;

public interface RecipeProcessDao extends JpaRepository<RecipeProcess, String>{
	RecipeProcess findRecipeProcessByRecipeId(int recipe_id);
	List<RecipeProcess> findByRecipeId(Recipe recipeId);
}
