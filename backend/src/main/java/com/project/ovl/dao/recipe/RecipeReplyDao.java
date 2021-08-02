package com.project.ovl.dao.recipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.recipe.RecipeReply;
import com.project.ovl.model.user.User;

public interface RecipeReplyDao extends JpaRepository<RecipeReply, String> {
	RecipeReply findByRecipeReplyId(int recipe_reply_id);
	List<RecipeReply> findByRecipeCommentId(RecipeComment rc);
	List<RecipeReply> findByUserId(User user);
}
