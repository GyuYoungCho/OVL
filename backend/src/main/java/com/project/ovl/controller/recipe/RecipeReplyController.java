package com.project.ovl.controller.recipe;

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

import com.project.ovl.dao.recipe.RecipeCommentDao;
import com.project.ovl.dao.recipe.RecipeDao;
import com.project.ovl.dao.recipe.RecipeReplyDao;
import com.project.ovl.dao.recipe.RecipeReplyLikeDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.model.like.RecipeReplyLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.recipe.RecipeReply;
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
	
	@Autowired
	RecipeCommentDao recipeCommentDao;
	
	@Autowired
	RecipeReplyDao recipeReplyDao;
	
	@Autowired
	RecipeReplyLikeDao recipeReplyLikeDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "대댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("commentId") int comment_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		RecipeComment comment = recipeCommentDao.findByRecipeCommentId(comment_id);
		
		// 해당 댓글 reply_count +1
		comment.setReply_count(comment.getReply_count()+1);
		recipeCommentDao.save(comment);
		// 해당 게시글 comment_count+1
		Recipe recipe = recipeDao.findRecipeByRecipeId(comment.getRecipeId().getRecipeId());
		recipe.setCommentcount(recipe.getCommentcount()+1);
		recipeDao.save(recipe);
		
		recipeReplyDao.save(new RecipeReply(0, content, 0, new Date(), comment, user));
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "대댓글 수정")
	public ResponseEntity<String> modify(@RequestParam("reply_id") int reply_id, @RequestParam("content") String content) {
		RecipeReply reply = recipeReplyDao.findByRecipeReplyId(reply_id);
		reply.setContent(content);
		
		recipeReplyDao.save(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{reply_id}")
	@ApiOperation(value = "대댓글 삭제")
	public ResponseEntity<String> delete(@PathVariable int reply_id) {
		RecipeReply reply = recipeReplyDao.findByRecipeReplyId(reply_id);
		
		// 대댓글 좋아요 삭제
		List<RecipeReplyLike> likeList = recipeReplyLikeDao.findAll();
		for (RecipeReplyLike rrl : likeList) {
			if (rrl.getRecipeReplyId().getRecipeReplyId()==reply_id) recipeReplyLikeDao.delete(rrl);
		}
		
		// 해당 댓글 reply_count -1
		RecipeComment comment = reply.getRecipeCommentId();
		comment.setReply_count(comment.getReply_count()-1);
		recipeCommentDao.save(comment);
		// 해당 게시글 comment_count -1
		Recipe recipe = recipeDao.findRecipeByRecipeId(comment.getRecipeId().getRecipeId());
		recipe.setCommentcount(recipe.getCommentcount()-1);
		recipeDao.save(recipe);
		
		recipeReplyDao.delete(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/select_all/{comment_id}")
	@ApiOperation(value = "모든 대댓글 조회")
	public ResponseEntity<List<RecipeReply>> seelct_all(@PathVariable int comment_id) {
		List<RecipeReply> replyList = recipeReplyDao.findAll();
		List<RecipeReply> returnList = new ArrayList<>();
		
		for (RecipeReply pp : replyList) {
			if (pp.getRecipeCommentId().getRecipeCommentId() == comment_id) returnList.add(pp);
		}
		
		return new ResponseEntity<List<RecipeReply>>(returnList, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{reply_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int reply_id) {
		User user = userDao.getUserByUserid(user_id);
		RecipeReply reply = recipeReplyDao.findByRecipeReplyId(reply_id);
		RecipeReplyLike like = recipeReplyLikeDao.findRecipeReplyLikeByUserIdAndRecipeReplyId(user, reply);
		 
		// like 테이블에 존재하는지 확인
		if (like==null) { // 존재하지 않을 시
			// 해당 reply like_count+1
			reply.setLike_count(reply.getLike_count()+1);
			
			// like 테이블에 저장
			recipeReplyLikeDao.save(new RecipeReplyLike(0, user, reply));
		} else { // 이미 존재 시
			// 해당 reply like_count-1
			reply.setLike_count(reply.getLike_count()-1);
			
			recipeReplyLikeDao.delete(like);
		}
		recipeReplyDao.save(reply);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 대댓글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<RecipeReplyLike> likeList = recipeReplyLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// 좋아요 데이터에 있는 user_id와 나의 user_id가 일치할 시 reply_id를 set에 저장
		for (RecipeReplyLike prl : likeList) {
			if (prl.getUserId().getUserid()==user_id) returnSet.add(prl.getRecipeReplyId().getRecipeReplyId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
