package com.project.ovl.dao.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.like.PostReplyLike;
import com.project.ovl.model.post.PostReply;
import com.project.ovl.model.user.User;

public interface PostReplyLikeDao extends JpaRepository<PostReplyLike,String>{
	PostReplyLike findPostReplyLikeByUserIdAndPostReplyId(User userId, PostReply replyId);
	List<PostReplyLike> findByUserId(User userId);
	List<PostReplyLike> findByPostReplyId(PostReply pr);
}
