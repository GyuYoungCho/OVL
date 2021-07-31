package com.project.ovl.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.RecipeComment;

public interface RecipeCommentDao extends JpaRepository<RecipeComment, String>{
	RecipeComment findByRecipeCommentId(int recipe_comment_id);
	Optional<List<RecipeComment>> findByRecipeIdRecipeId(int RecipeId);
}
