package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.RecipeLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

public interface RecipeLikeDao  extends JpaRepository<RecipeLike, Integer>{
	RecipeLike findReipceLikeByUserIdAndRecipeId(User userId, Recipe recipeId);
}
