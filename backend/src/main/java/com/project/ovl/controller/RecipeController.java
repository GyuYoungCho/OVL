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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeLikeDao;
import com.project.ovl.dao.RecipeProcessDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.like.RecipeLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipePhotoHandler;
import com.project.ovl.model.recipe.RecipeProcess;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recipe")
@CrossOrigin("*")
public class RecipeController {
	private static final String SUCCESS = "success";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	RecipeLikeDao recipeLikeDao;
	
	@Autowired
	RecipeProcessDao processDao;
	
	@Autowired
	RecipePhotoHandler photoHandler;
	
	private List<String> processContent;
	
	@PostMapping("/regist")
	@ApiOperation(value = "레시피 등록")
	public ResponseEntity<String> regist(@RequestPart("title") String title, @RequestPart("content") String content, @RequestPart("ingredient") String ingredient, @RequestPart("userId") String userId,
										@RequestPart("picture") MultipartFile pic, @RequestPart("files") List<MultipartFile> files) throws Exception {
		
		User user = userDao.getUserByUserid(Integer.parseInt(userId));
		
		// 레시피 등록
		Recipe recipe = new Recipe(0, title, content, ingredient, new Date(), 0, 0, null, null, user);
		recipeDao.save(recipe);
		
		photoHandler.saveProfile(pic, recipe.getRecipeId());
		
		// 레시피 과정 등록
		List<RecipeProcess> processList = photoHandler.getProcessList(files, processContent, recipe.getRecipeId());

		for (RecipeProcess pl : processList) {
			processDao.save(pl);
		}
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PostMapping("/regist/contentLisst")
	@ApiOperation(value = "레시피 과정 설명 등록")
	public ResponseEntity<String> contentList(@RequestBody List<String> contentList) {
		processContent = contentList;
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{recipe_id}")
	@ApiOperation(value = "레시피 삭제")
	public ResponseEntity<String> delete(@PathVariable int recipe_id) {
		// 레시피 과정 삭제
		List<RecipeProcess> processList = processDao.findAll();
		
		for (RecipeProcess rp : processList) {
			if (rp.getRecipeId().getRecipeId() == recipe_id) processDao.delete(rp);
		}
		
		// 레시피 삭제
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		recipeDao.delete(recipe);
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/select_all")
	@ApiOperation(value = "레시피 전체 조회")
	public ResponseEntity<List<Recipe>> select_all() {
		List<Recipe> recipeList = recipeDao.findAll();
		
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
	}
	
	@GetMapping("/select_detail/{recipe_id}")
	@ApiOperation(value = "레시피 상세 조회")
	public ResponseEntity<List<RecipeProcess>> select_detail(@PathVariable int recipe_id) {
		List<RecipeProcess> processList = processDao.findAll();
		List<RecipeProcess> returnList = new ArrayList<>();
		
		for (RecipeProcess rp : processList) {
			if (rp.getRecipeId().getRecipeId() == recipe_id) returnList.add(rp);
		}
		
		return new ResponseEntity<List<RecipeProcess>>(returnList, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{recipe_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int recipe_id) {
		User user = userDao.getUserByUserid(user_id);
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		RecipeLike like = recipeLikeDao.findReipceLikeByUserIdAndRecipeId(user, recipe);
		 
		// like 테이블에 존재하는지 확인
		if (like==null) { // 존재하지 않을 시
			// 해당 recipe like_count +1
			recipe.setLike_count(recipe.getLike_count()+1);
			
			// like 테이블에 저장
			recipeLikeDao.save(new RecipeLike(0, user, recipe));
		} else { // 이미 존재 시
			// 해당 recipe like_count -1
			recipe.setLike_count(recipe.getLike_count()-1);
			
			recipeLikeDao.delete(like);
		}
		recipeDao.save(recipe);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 레시피 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<RecipeLike> likeList = recipeLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// 좋아요 데이터에 있는 user_id와 나의 user_id가 일치할 시 recipe_id를 set에 저장
		for (RecipeLike pl : likeList) {
			if (pl.getUserId().getUserid()==user_id) returnSet.add(pl.getRecipeId().getRecipeId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
