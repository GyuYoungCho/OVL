package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.post.PostReply;
import com.project.ovl.model.user.User;

public interface PostReplyDao extends JpaRepository<PostReply, String>{
	PostReply findByPostReplyId(int post_reply_id);
	List<PostReply> findByUserId(User userId);
	List<PostReply> findByPostCommentId(PostComment pc);
}
