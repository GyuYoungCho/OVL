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
import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.PostReplyDao;
import com.project.ovl.dao.PostReplyLikeDao;
import com.project.ovl.dao.UserDao;
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
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	PostDao postDao;
	
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
		// 해당 게시글 comment_count+1
		Post post = postDao.findPostByPostId(comment.getPostId().getPostId());
		post.setComment_count(post.getComment_count()+1);
		postDao.save(post);
		
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
		// 해당 게시글 comment_count -1
		Post post = postDao.findPostByPostId(comment.getPostId().getPostId());
		post.setComment_count(post.getComment_count()-1);
		postDao.save(post);
		
		postReplyDao.delete(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/select_all/{comment_id}")
	@ApiOperation(value = "모든 대댓글 조회")
	public ResponseEntity<List<PostReply>> seelct_all(@PathVariable int comment_id) {
		List<PostReply> replyList = postReplyDao.findAll();
		List<PostReply> returnList = new ArrayList<>();
		
		for (PostReply pp : replyList) {
			if (pp.getPostCommentId().getPostCommentId() == comment_id) returnList.add(pp);
		}
		
		return new ResponseEntity<List<PostReply>>(returnList, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{reply_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int reply_id) {
		User user = userDao.getUserByUserid(user_id);
		PostReply reply = postReplyDao.findByPostReplyId(reply_id);
		PostReplyLike like = postReplyLikeDao.findPostReplyLikeByUserIdAndPostReplyId(user, reply);
		 
		// like 테이블에 존재하는지 확인
		if (like==null) { // 존재하지 않을 시
			// 해당 reply like_count+1
			reply.setLike_count(reply.getLike_count()+1);
			
			// like 테이블에 저장
			postReplyLikeDao.save(new PostReplyLike(0, user, reply));
		} else { // 이미 존재 시
			// 해당 reply like_count-1
			reply.setLike_count(reply.getLike_count()-1);
			
			postReplyLikeDao.delete(like);
		}
		postReplyDao.save(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 대댓글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<PostReplyLike> likeList = postReplyLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// 좋아요 데이터에 있는 user_id와 나의 user_id가 일치할 시 reply_id를 set에 저장
		for (PostReplyLike prl : likeList) {
			if (prl.getUserId().getUserid()==user_id) returnSet.add(prl.getPostReplyId().getPostReplyId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
