package com.project.ovl.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/follow")
@CrossOrigin("*")
public class FollowController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
    FollowDao followDao;
	@Autowired
    UserDao userDao;
	
	
//	@PostMapping("/follow/{id}")
//	public ResponseEntity<String> follow(@PathVariable int from_id, @PathVariable int to_id) {
//		User fromUser = userDao.findByUserid(from_id);
//		Optional<User> oToUser = userDao.findByUserid(to_id);
//		User toUser = oToUser.get();
//
//		Follow follow = new Follow();
//		follow.setFromUser(fromUser);
//		follow.setToUser(toUser);
//
//		mFollowRepository.save(follow);
//
//		return "ok";
//	}

//	@DeleteMapping("/follow/{id}")
//	public @ResponseBody String unFollow(@AuthenticationPrincipal MyUserDetail userDetail, @PathVariable int id) {
//		User fromUser = userDetail.getUser();
//		Optional<User> oToUser = mUserRepository.findById(id);
//		User toUser = oToUser.get();
//
//		mFollowRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
//
//		List<Follow> follows = mFollowRepository.findAll();
//		return "ok"; // ResponseEntity로 수정
//	}
	
	@GetMapping("/select_following/{user_id}")
	@ApiOperation(value = "내가 팔로우하는 사람들")
	public ResponseEntity<List<Follow>> select_following(@PathVariable int user_id) {
		
		List<Follow> list = followDao.findByFromuserUserid(user_id);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
	
	@GetMapping("/select_follower/{user_id}")
	@ApiOperation(value = "나를 팔로우하는 사람들")
	public ResponseEntity<List<Follow>> select_follower(@PathVariable int user_id) {
		
		List<Follow> list = followDao.findByTouserUserid(user_id);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
//	
//	@GetMapping("/select_detail")
//	@ApiOperation(value = "나를 팔로우하는 사람들 상세 조회")
//	public ResponseEntity<List<User>> select_detail(@RequestBody Set<Integer> follow) {
//		
//		List<Integer> list = new ArrayList<>(follow);
//		List<User> targetList = userDao.findAllUserByUseridIn(list);
//		
//		return new ResponseEntity<>(targetList, HttpStatus.OK);
//		
//	}
}
