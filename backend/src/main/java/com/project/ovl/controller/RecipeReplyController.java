package com.project.ovl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.PostReplyDao;
import com.project.ovl.dao.PostReplyLikeDao;
import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeReplyDao;
import com.project.ovl.dao.RecipeReplyLikeDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.post.PostReply;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recipe/reply")
@CrossOrigin("*")
public class RecipeReplyController {
	private static final String SUCCESS = "success";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RecipeDao recipeDao;
	
//	@Autowired
//	RecipeCommentDao recipeCommentDao;
	
	@Autowired
	RecipeReplyDao recipeReplyDao;
	
	@Autowired
	RecipeReplyLikeDao recipeReplyLikeDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "대댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("commentId") int comment_id,
										@RequestParam("content") String content) {
		
//		User user = userDao.getUserByUserid(user_id);
////		RecipeComment comment = recipeCommentDao.findByPostCommentId(comment_id);
//		
//		// 해당 댓글 reply_count +1
//		comment.setReply_count(comment.getReply_count()+1);
//		postCommentDao.save(comment);
//		// 해당 게시글 comment_count+1
//		Recipe recipe = recipeDao.findRecipeByRecipeId(comment.getRecipeId().getRecipeId()
//		Post post = postDao.findPostByPostId(comment.getPostId().getPostId());
//		post.setComment_count(post.getComment_count()+1);
//		postDao.save(post);
//		
//		postReplyDao.save(new PostReply(0, content, 0, new Date(), comment, user));
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
}
