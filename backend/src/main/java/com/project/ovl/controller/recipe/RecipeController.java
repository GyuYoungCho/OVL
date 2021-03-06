package com.project.ovl.controller.recipe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.project.ovl.dao.challenge.ChallengeCertificationDao;
import com.project.ovl.dao.recipe.RecipeCommentDao;
import com.project.ovl.dao.recipe.RecipeDao;
import com.project.ovl.dao.recipe.RecipeLikeDao;
import com.project.ovl.dao.recipe.RecipeProcessDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.dao.user.UserLogDao;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.like.RecipeLike;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.recipe.RecipeProcess;
import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recipe")
@CrossOrigin("*")
public class RecipeController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserLogDao userLogDao;
	
	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	RecipeLikeDao recipeLikeDao;
	
	@Autowired
	RecipeCommentDao recipeCommentDao;
	
	@Autowired
	RecipeProcessDao recipeProcessDao;
	
	@Autowired
	RecipeProcessDao processDao;
	
	@Autowired
	RecipeCommentController commentController;
	
	@Autowired
	ChallengeCertificationDao challengeCertificationDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "????????? ??????")
	public ResponseEntity<Map<String,String>> regist(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("ingredient") String ingredient, @RequestParam("userId") String userId,
			@RequestParam("picPathList") List<String> picPathList, @RequestParam("processPathList") List<String> processPathList, @RequestParam("contentList") List<String> contentList) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		User user = userDao.getUserByUserid(Integer.parseInt(userId));
		
		// ????????? ?????? ??? ??????????????? ????????? 20??? ??????
		user.setExperience(user.getExperience()+20);
		
		// ????????? ??????
		Recipe recipe = new Recipe(0, title, content, ingredient, new Date(), 0, 0, picPathList.get(1), user);
		recipeDao.save(recipe);
		
		// ????????? ?????? ??????
		for (int i=1;i<processPathList.size();i++) {
			RecipeProcess process = new RecipeProcess(0, contentList.get(i), processPathList.get(i), recipe);
			recipeProcessDao.save(process);
		}
		
		// ????????? ?????? ?????? ??????
		if(user.getChallengeId().getChallengeId()!=1
				&& user.getChallengeId().getType()==2) {
			
			Challenge ch = user.getChallengeId();
			List<ChallengeCertification> cert = challengeCertificationDao.findByUserId(user);
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			
			for(ChallengeCertification cc : cert) {
				Date start = cc.getCertification_date();
				cal.setTime(start);
				cal.add(Calendar.DATE, ch.getCycle());
				Date end = cal.getTime();
				
				if(now.after(start) && now.before(end)) {
					
					if(cc.getCertification()==0) {
						cc.setCertification(1);
						challengeCertificationDao.save(cc);
						map.put("challenge", SUCCESS);
					}
					break;
				}
			}
		}
		
		// ?????? ?????? post??? type 1
		userLogDao.save(new UserLog(0, user, new Date(), 2 , 1,recipe.getRecipeId(), 20));

		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "????????? ??????")
	public ResponseEntity<String> modify(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("ingredient") String ingredient, @RequestParam("recipeId") String recipeId,
			@RequestParam("picPathList") List<String> picPathList, @RequestParam("modifyPathList") List<String> modifyPathList, @RequestParam("plusPathList") List<String> plusPathList,
			@RequestParam(value="deleteIdList", required=false) Set<Integer> deleteIdList, @RequestParam("modifyPhotoIdList") List<Integer> modifyPhotoIdList, 
			@RequestParam("modifyContentIdList") List<Integer> modifyContentIdList, @RequestParam("modifyContentList") List<String> modifyContentList,
			@RequestParam("plusContentList") List<String> plusContentList) throws Exception {
		
		// ????????? ????????? ??????
		Recipe recipe = recipeDao.findRecipeByRecipeId(Integer.parseInt(recipeId));
		recipe.setTitle(title);
		recipe.setContent(content);
		recipe.setIngredient(ingredient);
		if (picPathList.size()>1) recipe.setFilepath(picPathList.get(1));
		recipeDao.save(recipe);
		
		// ?????? ??????
		if (deleteIdList.size()>1) {
			List<RecipeProcess> processList = recipeProcessDao.findAll();
			for (RecipeProcess rp : processList) {
				if (deleteIdList.contains(rp.getRecipeProcessId())) recipeProcessDao.delete(rp);
			}
		}
		
		// ?????? ?????? ??????
		for (int i=1;i<modifyPhotoIdList.size();i++) {
			RecipeProcess process = recipeProcessDao.findRecipeProcessByRecipeProcessId(modifyPhotoIdList.get(i));
			process.setFilepath(modifyPathList.get(i));
			recipeProcessDao.save(process);
		}
		
		
		// ?????? ?????? ??????
		for (int i=1;i<modifyContentIdList.size();i++) {
			RecipeProcess process = recipeProcessDao.findRecipeProcessByRecipeProcessId(modifyContentIdList.get(i));
			process.setContent(modifyContentList.get(i));
			recipeProcessDao.save(process);
		}
		
		// ?????? ??????, ?????? ??????
		for (int i = 1; i < plusPathList.size(); i++) {
			RecipeProcess process = new RecipeProcess(0, plusContentList.get(i), plusPathList.get(i), recipe);
			recipeProcessDao.save(process);
		}
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{recipe_id}")
	@ApiOperation(value = "????????? ??????")
	public ResponseEntity<Map<String, String>> delete(@PathVariable int recipe_id) {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		
		// ?????? ?????????
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		
		// ?????? ????????? -20 
		User user = userDao.getUserByUserid(recipe.getUserid().getUserid());
		user.setExperience(user.getExperience()-20);
		userDao.save(user);
		
		// ?????? ????????? ????????? ????????? ??????
		List<RecipeLike> likeList = recipeLikeDao.findAll();
		
		for (RecipeLike rl : likeList) {
			if (rl.getRecipeId().getRecipeId()==recipe_id) recipeLikeDao.delete(rl);
		}
		
		// ?????? ????????? ?????? ??????
		List<RecipeProcess> processList = processDao.findAll();
		
		for (RecipeProcess rp : processList) {
			if (rp.getRecipeId().getRecipeId() == recipe_id) processDao.delete(rp);
		}
		
		// ?????? ????????? ?????? ??????
		List<RecipeComment> commentList = recipeCommentDao.findAll();
		
		for (RecipeComment rc : commentList) {
			if (rc.getRecipeId().getRecipeId()==recipe_id) commentController.delete(rc.getRecipeCommentId());
		}
		
		
		// ????????? ?????? ?????? ????????? ?????? ?????? ?????? ??????
		if(user.getChallengeId().getChallengeId()!=1
				&& user.getChallengeId().getType()==2) {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startDate = cal.getTime();
			cal.add(Calendar.DATE, user.getChallengeId().getCycle()-1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			Date endDate = cal.getTime();

			int count = recipeDao.countByUseridAndTimeBetween(user,startDate , endDate);
			
			// ????????? ?????? ??????
			if(count==1) {
				
				List<ChallengeCertification> cert = challengeCertificationDao.findByUserId(user);
				Date now = new Date();
				
				for(ChallengeCertification cc : cert) {
					
					Date start = cc.getCertification_date();
					cal.setTime(start);
					cal.add(Calendar.DATE, user.getChallengeId().getCycle());
					Date end = cal.getTime();
					
					if(now.after(start) && now.before(end)) {
						
						if(cc.getCertification()==1) {
							map.put("challenge", SUCCESS);
							cc.setCertification(0);
							challengeCertificationDao.save(cc);
						}
						break;
					}
				}
			}
		}
		UserLog log = userLogDao.findByTypeAndContentId(2, recipe_id);
		userLogDao.delete(log);
				
		
		// ????????? ??????
		recipeDao.delete(recipe);
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping("/select_all")
	@ApiOperation(value = "????????? ?????? ??????")
	public Page<Recipe> select_all(@RequestParam(required = false, defaultValue = "") String keyword, @RequestParam("cate") String cate, Pageable pageable) {
		keyword = "%" + keyword + "%";
		Pageable sortedByTimeDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(cate).descending()); 
		Page<Recipe> recipepage = recipeDao.findByKeyWord(keyword, sortedByTimeDesc);
		List<Recipe> recipeList = recipepage.toList();
		
		return new PageImpl<Recipe>(recipeList, sortedByTimeDesc ,recipepage.getTotalElements());
	}
	
	@GetMapping("/select_detail/{recipe_id}")
	@ApiOperation(value = "????????? ?????? ??????")
	public ResponseEntity<List<RecipeProcess>> select_detail(@PathVariable int recipe_id) {
		List<RecipeProcess> processList = processDao.findAll();
		List<RecipeProcess> returnList = new ArrayList<>();
		
		for (RecipeProcess rp : processList) {
			if (rp.getRecipeId().getRecipeId() == recipe_id) returnList.add(rp);
		}
		
		return new ResponseEntity<List<RecipeProcess>>(returnList, HttpStatus.OK);
	}
	
	@GetMapping("/search/{title_search}")
	@ApiOperation(value = "????????? ?????? ??????")
	public ResponseEntity<List<Recipe>> search(@PathVariable String title_search) {
		List<Recipe> recipeList = recipeDao.findByTitleContaining(title_search);
		
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
	}
	
	@GetMapping("/search_myrecipe/{user_id}")
	@ApiOperation(value = "?????? ????????? ??????")
	public ResponseEntity<List<Recipe>> myrecipes(@PathVariable int user_id) {
		User user = userDao.getUserByUserid(user_id);
		List<Recipe> recipeList = recipeDao.findByUserid(user);
		
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
	}
	
	@GetMapping("/like/{user_id}/{recipe_id}")
	@ApiOperation(value = "????????? ????????? or ??????")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int recipe_id) {
		User user = userDao.getUserByUserid(user_id);
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		RecipeLike like = recipeLikeDao.findReipceLikeByUserIdAndRecipeId(user, recipe);
		 
		// like ???????????? ??????????????? ??????
		if (like==null) { // ???????????? ?????? ???
			// ?????? recipe like_count +1
			recipe.setLikecount(recipe.getLikecount()+1);
			
			// like ???????????? ??????
			recipeLikeDao.save(new RecipeLike(0, user, recipe));
		} else { // ?????? ?????? ???
			// ?????? recipe like_count -1
			recipe.setLikecount(recipe.getLikecount()-1);
			
			recipeLikeDao.delete(like);
		}
		recipeDao.save(recipe);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "?????? ????????? ?????? ????????? ??????")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// ?????? ????????? ????????? ????????????
		List<RecipeLike> likeList = recipeLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// ????????? ???????????? ?????? user_id??? ?????? user_id??? ????????? ??? recipe_id??? set??? ??????
		for (RecipeLike pl : likeList) {
			if (pl.getUserId().getUserid()==user_id) returnSet.add(pl.getRecipeId().getRecipeId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
