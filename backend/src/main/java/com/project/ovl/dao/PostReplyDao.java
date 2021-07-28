package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.post.PostReply;

public interface PostReplyDao extends JpaRepository<PostReply, String>{
	PostReply findByPostReplyId(int post_reply_id);
}
