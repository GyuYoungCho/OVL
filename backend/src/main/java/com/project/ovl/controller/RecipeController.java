package com.project.ovl.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeProcessDao;
import com.project.ovl.dao.UserDao;
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
	RecipeProcessDao processDao;
	
	@Autowired
	RecipePhotoHandler photoHandler;
	
	private List<String> processContent;
	
	@PostMapping("/regist")
	@ApiOperation(value = "레시피 등록")
	public ResponseEntity<String> regist(@RequestPart("title") String title, @RequestPart("content") String content, @RequestPart("ingredient") String ingredient, @RequestPart("userId") String userId,
										@RequestPart("picture") MultipartFile pic, @RequestPart("files") List<MultipartFile> files) throws Exception {
		
		System.out.println("레시피 등록!!!");
		System.out.println("재료 : "+ingredient);
		
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
	
}
