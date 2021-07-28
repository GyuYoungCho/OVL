package com.project.ovl.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.dao.PostCommentLikeDao;
import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/post/comment")
@CrossOrigin("*")
public class PostCommentController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	PostCommentDao postCommentDao;
	
	@Autowired
	PostCommentLikeDao postCommentLikeDao;
	
	@Autowired
	PostDao postDao;
	
	@Autowired
	UserDao userDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("postId") int post_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		Post post = postDao.findPostByPostId(post_id);
		
		postCommentDao.save(new PostComment(0, content, 0, new Date(),post, user));
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/search_all/{post_id}")
	@ApiOperation(value = "포스트의 모든 댓글 조회")
	public ResponseEntity<List<PostComment>> select_all(@PathVariable int post_id) {
		Optional<List<PostComment>> list = postCommentDao.findByPostIdPostId(post_id);
		if (list.isPresent()) 
			return new ResponseEntity<>(list.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/modify/{post_comment_id}")
	@ApiOperation(value = "댓글 수정")
	public ResponseEntity<String> modify(@PathVariable int post_comment_id, @RequestParam("content") String content) {
		PostComment pc = postCommentDao.findByPostCommentId(post_comment_id);
		pc.setContent(content);
		postCommentDao.save(pc);
		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{post_comment_id}")
	@ApiOperation(value = "댓글 삭제")
	public ResponseEntity<String> unreport(@PathVariable int post_comment_id) {
		PostComment pc = postCommentDao.findByPostCommentId(post_comment_id);
		postCommentDao.delete(pc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/like_list/{userId}")
	@ApiOperation(value = "내가 좋아요 누른 게시글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<PostCommentLike> likeList = postCommentLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		for (PostCommentLike pcl : likeList) {
			if (pcl.getUserId().getUserid()==user_id)
				returnSet.add(pcl.getPostCommentId().getPostCommentId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{post_commnet_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int post_commnet_id) {
		User user = userDao.getUserByUserid(user_id);
		PostComment pc = postCommentDao.findByPostCommentId(post_commnet_id);
		PostCommentLike like = postCommentLikeDao.findByUserIdAndPostCommentId(user, pc);
		 
		
		postCommentDao.save(pc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
}
