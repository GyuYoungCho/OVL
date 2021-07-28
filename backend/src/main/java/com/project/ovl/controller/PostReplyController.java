package com.project.ovl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.dao.PostReplyDao;
import com.project.ovl.dao.UserDao;
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
	
	@PostMapping("/regist")
	@ApiOperation(value = "대댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("commentId") int comment_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		PostComment comment = postCommentDao.findByPostCommentId(comment_id);
		
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
		postReplyDao.delete(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
}
