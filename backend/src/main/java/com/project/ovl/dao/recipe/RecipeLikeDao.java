package com.project.ovl.dao.recipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.RecipeLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

public interface RecipeLikeDao  extends JpaRepository<RecipeLike, Integer>{
	RecipeLike findReipceLikeByUserIdAndRecipeId(User userId, Recipe recipeId);
	List<RecipeLike> findByRecipeId(Recipe recipeId);
	List<RecipeLike> findByUserId(User user);
}
