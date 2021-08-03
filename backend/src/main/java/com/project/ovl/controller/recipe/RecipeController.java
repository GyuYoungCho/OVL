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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.project.ovl.model.recipe.RecipePhotoHandler;
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
	RecipeProcessDao processDao;
	
	@Autowired
	RecipeCommentController commentController;
	
	@Autowired
	RecipePhotoHandler photoHandler;
	
	@Autowired
	ChallengeCertificationDao challengeCertificationDao;
	
	private List<String> processContent;
	
	@PostMapping("/regist")
	@ApiOperation(value = "레시피 등록")
	public ResponseEntity<Map<String,String>> regist(@RequestPart("title") String title, @RequestPart("content") String content, @RequestPart("ingredient") String ingredient, @RequestPart("userId") String userId,
										@RequestPart("picture") MultipartFile pic, @RequestPart("files") List<MultipartFile> files) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		User user = userDao.getUserByUserid(Integer.parseInt(userId));
		
		// 레시피 등록 시 사용자에게 경험치 20점 부여
		user.setExperience(user.getExperience()+20);
		
		// 레시피 등록
		Recipe recipe = new Recipe(0, title, content, ingredient, new Date(), 0, 0, null, null, user);
		recipeDao.save(recipe);
		
		photoHandler.saveProfile(pic, recipe.getRecipeId());
		
		
		// 레시피 과정 등록
		List<RecipeProcess> processList = photoHandler.getProcessList(files, processContent, recipe.getRecipeId());
		
		for (RecipeProcess rp : processList) {
			processDao.save(rp);
		}
		
		// 챌린지 중일 경우 인증
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
		
		// 로그 저장 post라 type 1
		userLogDao.save(new UserLog(0, user, new Date(), 2 , 1,recipe.getRecipeId(), 20));

		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PostMapping("/regist/contentList")
	@ApiOperation(value = "레시피 과정 설명 등록 또는 수정")
	public ResponseEntity<String> contentList(@RequestBody List<String> contentList) {
		processContent = contentList;
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "레시피 수정")
	public ResponseEntity<String> modify(@RequestPart("title") String title, @RequestPart("content") String content, @RequestPart("ingredient") String ingredient,
			@RequestPart("picture") MultipartFile pic, @RequestPart("files") List<MultipartFile> files, @RequestPart("recipeId") String recipeId) throws Exception {
		
		// 레시피 게시물 수정
		Recipe recipe = recipeDao.findRecipeByRecipeId(Integer.parseInt(recipeId));
		recipe.setTitle(title);
		recipe.setContent(content);
		recipe.setIngredient(ingredient);
		recipeDao.save(recipe);
		
		photoHandler.saveProfile(pic, recipe.getRecipeId());
		
		// 레시피 과정 사진 지우기
		List<RecipeProcess> proList = processDao.findAll();
		for (RecipeProcess rp : proList) {
			if (rp.getRecipeId().getRecipeId()==recipe.getRecipeId()) processDao.delete(rp);
		}
		
		// 레시피 사진 다시 저장
		List<RecipeProcess> saveList = photoHandler.getProcessList(files, processContent, recipe.getRecipeId());

		for (RecipeProcess rp : saveList) {
			processDao.save(rp);
		}
		
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{recipe_id}")
	@ApiOperation(value = "레시피 삭제")
	public ResponseEntity<Map<String, String>> delete(@PathVariable int recipe_id) {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		
		// 해당 레시피
		Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
		
		// 유저 경험치 -20 
		User user = userDao.getUserByUserid(recipe.getUserid().getUserid());
		user.setExperience(user.getExperience()-20);
		userDao.save(user);
		
		// 해당 레시피 좋아요 리스트 삭제
		List<RecipeLike> likeList = recipeLikeDao.findAll();
		
		for (RecipeLike rl : likeList) {
			if (rl.getRecipeId().getRecipeId()==recipe_id) recipeLikeDao.delete(rl);
		}
		
		// 해당 레시피 과정 삭제
		List<RecipeProcess> processList = processDao.findAll();
		
		for (RecipeProcess rp : processList) {
			if (rp.getRecipeId().getRecipeId() == recipe_id) processDao.delete(rp);
		}
		
		// 해당 레시피 댓글 삭제
		List<RecipeComment> commentList = recipeCommentDao.findAll();
		
		for (RecipeComment rc : commentList) {
			if (rc.getRecipeId().getRecipeId()==recipe_id) commentController.delete(rc.getRecipeCommentId());
		}
		
		
		// 챌린지 중일 경우 조건에 따른 인증 취소 처리
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
			
			// 하나일 경우 취소
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
				
		
		// 레시피 삭제
		recipeDao.delete(recipe);
		
		return new ResponseEntity<>(map, HttpStatus.OK);
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
	
	@GetMapping("/search/{title_search}")
	@ApiOperation(value = "레시피 제목 검색")
	public ResponseEntity<List<Recipe>> search(@PathVariable String title_search) {
		List<Recipe> recipeList = recipeDao.findByTitleContaining(title_search);
		
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
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
