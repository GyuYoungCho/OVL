package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.RecipeReply;

public interface RecipeReplyDao extends JpaRepository<RecipeReply, String> {
	RecipeReply findByRecipeReplyId(int recipe_reply_id);
}
