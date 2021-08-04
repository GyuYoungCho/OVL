package com.project.ovl.dao.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.user.User;

public interface PostCommentDao extends JpaRepository<PostComment, String>{
	PostComment findByPostCommentId(int post_comment_id);
	Optional<List<PostComment>> findByPostIdPostId(int PostId);
	List<PostComment> findByUserId(User userId);
}
