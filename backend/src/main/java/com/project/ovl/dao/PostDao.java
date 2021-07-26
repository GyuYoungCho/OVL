package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.Post;

public interface PostDao extends JpaRepository<Post, String> {

}
