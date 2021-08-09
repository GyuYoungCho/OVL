package com.project.ovl.controller.post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.challenge.ChallengeCertificationDao;
import com.project.ovl.dao.challenge.ChallengeDao;
import com.project.ovl.dao.post.PostCommentDao;
import com.project.ovl.dao.post.PostDao;
import com.project.ovl.dao.post.PostLIkeDao;
import com.project.ovl.dao.post.PostPhotoDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.dao.user.UserLogDao;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.photo.PhotoHandler;
import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	PostDao postDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserLogDao userLogDao;
	
	@Autowired
    FollowDao followDao;
	
	@Autowired
    PostLIkeDao postLikeDao;
	
	@Autowired
	PostPhotoDao postPhotoDao;
	
	@Autowired
	PostCommentDao postCommentDao;
	
	@Autowired
	PostCommentController commentController;
	
	@Autowired
	PhotoHandler photoHandler;
	
	@Autowired
	ChallengeDao challengeDao;
	
	@Autowired
	ChallengeCertificationDao challengeCertificationDao;
	
	private Set<Integer> deleteId;
	private List<Integer> modifyId;
	
	@PostMapping("/regist")
	@ApiOperation(value = "게시글 등록")
	public ResponseEntity<Map<String, String>> regist(@RequestPart("files") List<MultipartFile> files, @RequestPart("category") String category,
											@RequestPart("content") String content, @RequestPart("userId") String userId) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		
		// 게시글 저장
		User user = userDao.getUserByUserid(Integer.parseInt(userId));
		// 게시글 등록 시 유저에 경험치 5 추가
		int value = 0;
		if (Integer.parseInt(category)==1) value = 5;
		else if (Integer.parseInt(category)==2) value = 3;
		else value = 2;
		
		user.setExperience(user.getExperience()+value);
		userDao.save(user);
		
		// 게시글 등록
		Post post = new Post(0, Integer.parseInt(category), content, 0, 0, new Date(), user);
		postDao.save(post);
		// 이미지 저장
		photoHandler.parseFileInfo(files, post.getPostId(), 0, null);
		
		// 챌린지 중일 경우 인증
		if(user.getChallengeId().getChallengeId()!=1
				&& user.getChallengeId().getType()==1
				&& user.getChallengeId().getCategory()==Integer.parseInt(category)) {
			
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
		userLogDao.save(new UserLog(0, user, new Date(), 1 , Integer.parseInt(category),post.getPostId(), value));
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PostMapping("/modifyList")
	@ApiOperation(value="게시글 수정 관련 리스트 가져오기")
	public ResponseEntity<String> modifyList(@RequestParam(value="deleteIdList", required=false) Set<Integer> deleteIdList, @RequestParam(value="modifyIdList", required=false) List<Integer> modifyIdList) {
		deleteId = deleteIdList;
		modifyId = modifyIdList;
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "게시글 수정")
	public ResponseEntity<Map<String, String>> modify(@RequestPart("modifyPhotoList") List<MultipartFile> modifyPhotoList, @RequestPart("plusPhotoList") List<MultipartFile> plusPhotoList,
			@RequestPart("category") String category, @RequestPart("content") String content, @RequestPart("postId") String postId) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		
		Post post = postDao.findPostByPostId(Integer.parseInt(postId));
		User user = post.getUserId();
		int user_chall_cate = user.getChallengeId().getCategory();
		
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
		
		int count = postDao.countByUserIdAndCategoryAndTimeBetween(
				user,user_chall_cate,startDate,endDate);
		// 카테고리 변경에 따른 챌린지 처리
		if(Integer.parseInt(category)!=post.getCategory()) {
			
			
			// 챌린지로 인증됨
			if(user_chall_cate==Integer.parseInt(category) && count ==0) {
				List<ChallengeCertification> cert = challengeCertificationDao.findByUserId(user);
				Date now = new Date();
				
				for(ChallengeCertification cc : cert) {
					
					Date start = cc.getCertification_date();
					cal.setTime(start);
					cal.add(Calendar.DATE, user.getChallengeId().getCycle());
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
				
			// 챌린지 인증 취소됨
			if(user_chall_cate==post.getCategory() && count ==1) {
				List<ChallengeCertification> cert = challengeCertificationDao.findByUserId(user);
				Date now = new Date();
				
				for(ChallengeCertification cc : cert) {
					
					Date start = cc.getCertification_date();
					cal.setTime(start);
					cal.add(Calendar.DATE, user.getChallengeId().getCycle());
					Date end = cal.getTime();
					
					if(now.after(start) && now.before(end)) {
						
						if(cc.getCertification()==1) {
							cc.setCertification(0);
							challengeCertificationDao.save(cc);
							map.put("challenge", SUCCESS);
						}
						break;
					}
				}
			}
			
			UserLog log = userLogDao.findByTypeAndContentId(1, Integer.parseInt(postId));
			log.setCategory(Integer.parseInt(category));
			userLogDao.save(log);
		}
		
		
		post.setCategory(Integer.parseInt(category));
		post.setContent(content);
		
		postDao.save(post);
		
		// 사진 삭제
		if (deleteId.size()>0) {
			List<PostPhoto> deleteList = postPhotoDao.findAll();
			for (PostPhoto pp : deleteList) {
				if (deleteId.contains(pp.getPostPhotoId())) postPhotoDao.delete(pp);
			}
		}
		
		// 사진 수정
		if (modifyPhotoList.size()>0) photoHandler.parseFileInfo(modifyPhotoList, Integer.parseInt(postId), 1, modifyId);
		
		// 사진 추가
		if (modifyPhotoList.size()>0) photoHandler.parseFileInfo(plusPhotoList, Integer.parseInt(postId), 0, null);
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	} 
	
	@DeleteMapping("/delete/{post_id}")
	@ApiOperation(value = "게시글 삭제")
	public ResponseEntity<Map<String, String>> delete(@PathVariable int post_id) {
		
		Map<String, String> map = new HashMap<>();
		map.put("job", SUCCESS);
		map.put("challenge", FAIL);
		
		
		// 해당 게시글 올린 유저 경험치 -5
		Post post = postDao.findPostByPostId(post_id);
		
		int value = 0;
		if (post.getCategory()==1) value = 5;
		else if (post.getCategory()==2) value = 3;
		else value = 2;
		
		User user = userDao.getUserByUserid(post.getUserId().getUserid());
		user.setExperience(user.getExperience()-value);
		userDao.save(user);
		
		// 해당 게시글 좋아요 리스트 삭제
		List<PostLike> likeList = postLikeDao.findAll();
		
		for (PostLike pl : likeList) {
			if (pl.getPostId().getPostId()==post_id) postLikeDao.delete(pl);
		}
		
		// 해당 게시글 사진 리스트 삭제
		List<PostPhoto> photoList = postPhotoDao.findAll();
		
		for (PostPhoto pp : photoList) {
			if (pp.getPostId().getPostId()==post_id) postPhotoDao.delete(pp);
		}
		
		// 해당 게시글 댓글 삭제
		List<PostComment> commentList = postCommentDao.findAll();
		
		for (PostComment pc : commentList) {
			if (pc.getPostId().getPostId()==post_id) commentController.delete(pc.getPostCommentId());
		}
		
		
		// 챌린지 중일 경우 조건에 따른 인증 취소 처리
		if(user.getChallengeId().getChallengeId()!=1
				&& user.getChallengeId().getType()==1
				&& user.getChallengeId().getCategory()==post.getCategory()) {
			
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

			int count = postDao.countByUserIdAndCategoryAndTimeBetween(
					user,post.getCategory(),startDate , endDate);
			
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
							cc.setCertification(0);
							challengeCertificationDao.save(cc);
							map.put("challenge", SUCCESS);
						}
						break;
					}
				}
			}
		}
		UserLog log = userLogDao.findByTypeAndContentId(1, post_id);
		userLogDao.delete(log);
		
		postDao.delete(post);
		
		// 로그 삭제 post라 type 1
		
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	} 
	
	@GetMapping("/select_all/{user_id}")
	@ApiOperation(value = "게시글 조회")
	public ResponseEntity<List<PostPhoto>> select_all(@PathVariable int user_id) {
		// 내가 팔로우 한 사람 찾기
		Optional<List<Follow>> followList = followDao.findByFromIdUserid(user_id);
		Set<Integer> followingList = new HashSet<>();
		if (followList.isPresent()) {
			for (Follow r : followList.get()) {
				followingList.add(r.getToId().getUserid());
			}
		}

		// 내가 팔로우 한 사람의 게시글만 저장해서 보내주기
		List<Post> postList = postDao.findAll();
		// 해당 게시글의 이미지 리스트를 찾기 위해 이미지 데이터 싹 가져오기
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		for (Post p : postList) {
			if (followingList.contains(p.getUserId().getUserid()) || p.getUserId().getUserid() == user_id) {
				List<PostPhoto> saveList = new ArrayList<>();
				// 해당 게시물의 이미지를 리스트에 저장
				for (PostPhoto pp : photoList) {
					if (pp.getPostId().getPostId()==p.getPostId()) saveList.add(pp);
				}
				
				Collections.sort(saveList, (o1, o2)-> {
					return Integer.compare(o1.getPostPhotoId(), o2.getPostPhotoId());
				});
				
				returnList.add(saveList.get(0));
			}
		}
		// 최신글을 먼저 보여줘야 하므로 post_id 역순으로 정렬 
		Collections.sort(returnList, (o1, o2)-> {
			return Integer.compare(o2.getPostPhotoId(), o1.getPostPhotoId());
		});
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
	} 

	@GetMapping("/select_user/{user_id}")
	@ApiOperation(value = "자기 자신 게시글 조회")
	public ResponseEntity<List<PostPhoto>> select_user(@PathVariable int user_id) {
		// 내가 작성한 게시글만 저장해서 보내주기
		List<Post> postList = postDao.findAll();
		// 해당 게시글의 이미지 리스트를 찾기 위해 이미지 데이터 싹 가져오기
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		for (Post p : postList) {
			if (p.getUserId().getUserid() == user_id) {
				List<PostPhoto> saveList = new ArrayList<>();
				// 해당 게시물의 이미지를 리스트에 저장
				for (PostPhoto pp : photoList) {
					if (pp.getPostId().getPostId()==p.getPostId()) saveList.add(pp);
				}
				
				Collections.sort(saveList, (o1, o2)-> {
					return Integer.compare(o1.getPostPhotoId(), o2.getPostPhotoId());
				});
				
				returnList.add(saveList.get(0));
			}
		}
		// 최신글을 먼저 보여줘야 하므로 post_id 역순으로 정렬 
		Collections.sort(returnList, (o1, o2)-> {
			return Integer.compare(o2.getPostPhotoId(), o1.getPostPhotoId());
		});
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail/{post_id}")
	@ApiOperation(value = "게시글 상세조회")
	public ResponseEntity<Post> select_detail(@PathVariable int post_id) {
		Post detailPost = postDao.findPostByPostId(post_id);
		return new ResponseEntity<Post>(detailPost, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail_photo/{post_id}")
	@ApiOperation(value = "게시글 이미지 상세조회")
	public ResponseEntity<List<PostPhoto>> select_detail_photo(@PathVariable int post_id) {
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		
		for (PostPhoto pl : photoList) {
			if (pl.getPostId().getPostId()==post_id) returnList.add(pl);
		}
		
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
	} 
	
	@GetMapping("/like/{user_id}/{post_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int post_id) {
		User user = userDao.getUserByUserid(user_id);
		Post post = postDao.findPostByPostId(post_id);
		PostLike like = postLikeDao.findPostLikeByUserIdAndPostId(user, post);
		 
		// like 테이블에 존재하는지 확인
		if (like==null) { // 존재하지 않을 시
			// 해당 post like_count+1
			post.setLike_count(post.getLike_count()+1);
			
			// like 테이블에 저장
			postLikeDao.save(new PostLike(0, user, post));
		} else { // 이미 존재 시
			// 해당 post like_count-1
			post.setLike_count(post.getLike_count()-1);
			
			postLikeDao.delete(like);
		}
		postDao.save(post);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 게시글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<PostLike> likeList = postLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// 좋아요 데이터에 있는 user_id와 나의 user_id가 일치할 시 post_id를 set에 저장
		for (PostLike pl : likeList) {
			if (pl.getUserId().getUserid()==user_id) returnSet.add(pl.getPostId().getPostId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
