package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.user.User;

public interface PostCommentLikeDao extends JpaRepository<PostCommentLike,String>{
	PostCommentLike findByUserIdAndPostCommentId(User userId, PostComment commentId);
	List<PostCommentLike> findByUserId(User userId);
	List<PostCommentLike> findByPostCommentId(PostComment pc);
}
