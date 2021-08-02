package com.project.ovl.dao.recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.user.User;

public interface RecipeCommentDao extends JpaRepository<RecipeComment, String>{
	RecipeComment findByRecipeCommentId(int recipe_comment_id);
	Optional<List<RecipeComment>> findByRecipeIdRecipeId(int RecipeId);
	List<RecipeComment> findByUserId(User user);
}
