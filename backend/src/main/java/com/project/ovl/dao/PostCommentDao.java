package com.project.ovl.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.PostComment;

public interface PostCommentDao extends JpaRepository<PostComment, String>{
	PostComment getByPostcommentid(int post_comment_id);
//	Optional<List<PostComment>> findByPostPostid(int post_id);
}
