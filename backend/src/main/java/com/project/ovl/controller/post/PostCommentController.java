package com.project.ovl.controller.post;

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

import com.project.ovl.dao.post.PostCommentDao;
import com.project.ovl.dao.post.PostCommentLikeDao;
import com.project.ovl.dao.post.PostDao;
import com.project.ovl.dao.post.PostReplyDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.like.RecipeCommentLike;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.post.PostReply;
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
	
	@Autowired
	PostReplyDao postReplyDao;
	
	@Autowired
	PostReplyController replyController;
	
	@PostMapping("/regist")
	@ApiOperation(value = "댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("postId") int post_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		Post post = postDao.findPostByPostId(post_id);
		
		
		postCommentDao.save(new PostComment(0, content, 0,0, new Date(),post, user));
		post.setComment_count(post.getComment_count()+1);
		postDao.save(post);
		
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
	
	@PutMapping("/modify")
	@ApiOperation(value = "댓글 수정")
	public ResponseEntity<String> modify(@RequestParam("comment_id") int post_comment_id, @RequestParam("content") String content) {
		PostComment pc = postCommentDao.findByPostCommentId(post_comment_id);
		pc.setContent(content);
		postCommentDao.save(pc);
		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{post_comment_id}")
	@ApiOperation(value = "댓글 삭제")
	public ResponseEntity<String> delete(@PathVariable int post_comment_id) {
		PostComment pc = postCommentDao.findByPostCommentId(post_comment_id);

		Post post = postDao.findPostByPostId(pc.getPostId().getPostId());
		post.setComment_count(post.getComment_count()-1);
		
		// 해당 댓글의 좋아요 삭제
		List<PostCommentLike> likeList = postCommentLikeDao.findAll();
		for (PostCommentLike pcl : likeList) {
			if (pcl.getPostCommentId().getPostCommentId()==post_comment_id) postCommentLikeDao.delete(pcl);
		}
		
		// 해당 댓글의 대댓글 삭제
		List<PostReply> replyList = postReplyDao.findAll();
		
		for (PostReply pr : replyList) {
			if (pr.getPostCommentId().getPostCommentId()==post_comment_id) {
				replyController.delete(pr.getPostReplyId());
			}
		}
		
		postCommentDao.delete(pc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/like_list/{user_id}/{post_id}")
	@ApiOperation(value = "내가 좋아요 누른 댓글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id, @PathVariable int post_id) {
		// 모든 좋아요 데이터 가져오기
		List<PostCommentLike> likeList = postCommentLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		for (PostCommentLike pcl : likeList) {
			if (pcl.getUserId().getUserid()==user_id && pcl.getPostCommentId().getPostId().getPostId()==post_id)
				returnSet.add(pcl.getPostCommentId().getPostCommentId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{post_comment_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int post_comment_id) {
		User user = userDao.getUserByUserid(user_id);
		PostComment pc = postCommentDao.findByPostCommentId(post_comment_id);
		PostCommentLike like = postCommentLikeDao.findByUserIdAndPostCommentId(user, pc);
		 
		if (like==null) { // 존재하지 않을 시
			// 해당 post like_count+1
			pc.setLike_count(pc.getLike_count()+1);
			
			// like 테이블에 저장
			postCommentLikeDao.save(new PostCommentLike(0, user, pc));
		} else { // 이미 존재 시
			// 해당 post like_count-1
			pc.setLike_count(pc.getLike_count()-1);
			
			postCommentLikeDao.delete(like);
		}
		
		postCommentDao.save(pc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
}
