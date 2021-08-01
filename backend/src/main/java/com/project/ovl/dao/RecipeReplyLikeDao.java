package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.RecipeReplyLike;
import com.project.ovl.model.recipe.RecipeReply;
import com.project.ovl.model.user.User;

public interface RecipeReplyLikeDao extends JpaRepository<RecipeReplyLike,String>{
	RecipeReplyLike findRecipeReplyLikeByUserIdAndRecipeReplyId(User userId, RecipeReply replyId);
	List<RecipeReplyLike> findByUserId(User user);
	List<RecipeReplyLike> findByRecipeReplyId(RecipeReply rr);
}
