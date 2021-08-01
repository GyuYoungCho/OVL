package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.Post;
import com.project.ovl.model.user.User;

public interface PostDao extends JpaRepository<Post, String> {
	Post findPostByPostId(int post_id);
	List<Post> findByUserId(User user);
}
