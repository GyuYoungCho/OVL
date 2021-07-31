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

import com.project.ovl.dao.RecipeCommentDao;
import com.project.ovl.dao.RecipeCommentLikeDao;
import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeReplyDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.like.RecipeCommentLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.recipe.RecipeReply;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recipe/comment")
@CrossOrigin("*")
public class RecipeCommentController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	RecipeCommentDao recipeCommentDao;
	
	@Autowired
	RecipeCommentLikeDao recipeCommentLikeDao;
	
	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RecipeReplyDao recipeReplyDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "댓글 등록")
	public ResponseEntity<String> regist(@RequestParam("userId") int user_id, @RequestParam("recipeId") int recipe_id,
										@RequestParam("content") String content) {
		
		User user = userDao.getUserByUserid(user_id);
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		
		
		recipeCommentDao.save(new RecipeComment(0, content, 0,0, new Date(),recipe, user));
		recipe.setComment_count(recipe.getComment_count()+1);
		recipeDao.save(recipe);
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/search_all/{recipe_id}")
	@ApiOperation(value = "레시피의 모든 댓글 조회")
	public ResponseEntity<List<RecipeComment>> select_all(@PathVariable int recipe_id) {
		Optional<List<RecipeComment>> list = recipeCommentDao.findByRecipeIdRecipeId(recipe_id);
		if (list.isPresent()) 
			return new ResponseEntity<>(list.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/modify/{recipe_comment_id}")
	@ApiOperation(value = "댓글 수정")
	public ResponseEntity<String> modify(@PathVariable int recipe_comment_id, @RequestParam("content") String content) {
		RecipeComment rc = recipeCommentDao.findByRecipeCommentId(recipe_comment_id);
		rc.setContent(content);
		recipeCommentDao.save(rc);
		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{recipe_comment_id}")
	@ApiOperation(value = "댓글 삭제")
	public ResponseEntity<String> unreport(@PathVariable int recipe_comment_id) {
		// 해당 댓글
		RecipeComment rc = recipeCommentDao.findByRecipeCommentId(recipe_comment_id);
		// 해당 레시피
		Recipe recipe = recipeDao.findRecipeByRecipeId(rc.getRecipeId().getRecipeId());
		recipe.setComment_count(recipe.getComment_count()-1); // 해당 레시피 댓글 삭제 시 레시피 comment_count -1
		
		// 해당 댓글의 대댓글 삭제
		List<RecipeReply> replyList = recipeReplyDao.findAll();
		
		for (RecipeReply rr : replyList) {
			if (rr.getRecipeCommentId().getRecipeCommentId()==recipe_comment_id) {
				recipeReplyDao.delete(rr);
				recipe.setComment_count(recipe.getComment_count()-1); // 해당 레시피 대댓글 삭제 시 레시피 comment_count -1
			}
		}
		
		recipeCommentDao.delete(rc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 레시피 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<RecipeCommentLike> likeList = recipeCommentLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		for (RecipeCommentLike pcl : likeList) {
			if (pcl.getUserId().getUserid()==user_id)
				returnSet.add(pcl.getRecipeCommentId().getRecipeCommentId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{recipe_comment_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int recipe_comment_id) {
		User user = userDao.getUserByUserid(user_id);
		RecipeComment rc = recipeCommentDao.findByRecipeCommentId(recipe_comment_id);
		RecipeCommentLike like = recipeCommentLikeDao.findByUserIdAndRecipeCommentId(user, rc);
		 
		if (like==null) { // 존재하지 않을 시
			// 해당 post like_count+1
			rc.setLike_count(rc.getLike_count()+1);
			
			// like 테이블에 저장
			recipeCommentLikeDao.save(new RecipeCommentLike(0, user, rc));
		} else { // 이미 존재 시
			// 해당 post like_count-1
			rc.setLike_count(rc.getLike_count()-1);
			
			recipeCommentLikeDao.delete(like);
		}
		
		recipeCommentDao.save(rc);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
}
