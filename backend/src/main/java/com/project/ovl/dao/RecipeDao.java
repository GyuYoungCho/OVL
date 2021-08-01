package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

public interface RecipeDao extends JpaRepository<Recipe, String>{
	Recipe findRecipeByRecipeId(int recipeId);
	List<Recipe> findByTitleContaining(String title);
	List<Recipe> findByUserid(User user);
}
