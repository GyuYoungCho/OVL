package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.user.User;

public interface PostCommentLikeDao extends JpaRepository<PostCommentLike,String>{
	PostCommentLike findByUserIdAndPostCommentId(User userId, PostComment commentId);
}
