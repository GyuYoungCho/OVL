package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.RecipeCommentLike;
import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.user.User;

public interface RecipeCommentLikeDao extends JpaRepository<RecipeCommentLike, String>{
	RecipeCommentLike findByUserIdAndRecipeCommentId(User userId, RecipeComment commentId);
}
