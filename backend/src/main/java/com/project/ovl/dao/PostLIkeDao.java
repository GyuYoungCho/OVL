package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.user.User;

public interface PostLIkeDao extends JpaRepository<PostLike, Integer>{
	PostLike findPostLikeByUserIdAndPostId(User userId, Post postId);
}
