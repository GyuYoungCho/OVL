package com.project.ovl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.dao.PostReplyDao;
import com.project.ovl.dao.PostReplyLikeDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.like.PostReplyLike;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.post.PostReply;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/post/reply")
@CrossOrigin("*")
public class PostReplyController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	PostCommentDao postCommentDao;
	
	@Autowired
	PostReplyDao postReplyDao;
	
	@Autowired
	PostReplyLikeDao postReplyLikeDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "대댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("commentId") int comment_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		PostComment comment = postCommentDao.findByPostCommentId(comment_id);
		
		// 해당 댓글 reply_count +1
		comment.setReply_count(comment.getReply_count()+1);
		postCommentDao.save(comment);
		
		postReplyDao.save(new PostReply(0, content, 0, new Date(), comment, user));
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "대댓글 수정")
	public ResponseEntity<String> modify(@RequestBody PostReply postReply) {
		postReplyDao.save(postReply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{reply_id}")
	@ApiOperation(value = "대댓글 삭제")
	public ResponseEntity<String> delete(@PathVariable int reply_id) {
		PostReply reply = postReplyDao.findByPostReplyId(reply_id);
		
		// 해당 댓글 reply_count -1
		PostComment comment = reply.getPostCommentId();
		comment.setReply_count(comment.getReply_count()-1);
		postCommentDao.save(comment);
		
		postReplyDao.delete(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/seelct_all/{comment_id}")
	@ApiOperation(value = "모든 대댓글 조회")
	public ResponseEntity<List<PostReply>> seelct_all(@PathVariable int comment_id) {
		List<PostReply> replyList = postReplyDao.findAll();
		List<PostReply> returnList = new ArrayList<>();
		
		for (PostReply pp : replyList) {
			if (pp.getPostCommentId().getPostCommentId() == comment_id) returnList.add(pp);
		}
		
		return new ResponseEntity<List<PostReply>>(returnList, HttpStatus.OK);
	}
}
