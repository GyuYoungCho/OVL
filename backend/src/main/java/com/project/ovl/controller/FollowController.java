//package com.project.ovl.controller;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.ovl.dao.FollowDao;
//import com.project.ovl.dao.UserDao;
//import com.project.ovl.model.follow.Follow;
//import com.project.ovl.model.user.User;
//
//import io.swagger.annotations.ApiOperation;
//
//@RestController
//@RequestMapping("/follow")
//@CrossOrigin("*")
//public class FollowController {
//	private static final String SUCCESS = "success";
//	private static final String FAIL = "fail";
//	
//	@Autowired
//    FollowDao followDao;
//	@Autowired
//    UserDao userDao;
//	
//	
//	@PostMapping("/follow/{from_id}/{to_id}")
//	@ApiOperation(value = "팔로우하기")
//	public ResponseEntity<String> follow(@PathVariable int from_id, @PathVariable int to_id) {
//		User fromUser = userDao.getUserByUserid(from_id);
//		User toUser =userDao.getUserByUserid(to_id);
//
//		Follow follow = new Follow();
//		follow.setFromuser(fromUser);
//		follow.setTouser(toUser);
//
//		followDao.save(follow);
//
//		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/follow/{from_id}/{to_id}")
//	@ApiOperation(value = "언팔하기")
//	public ResponseEntity<String> unFollow(@PathVariable int from_id, @PathVariable int to_id) {
//		User fromUser = userDao.getUserByUserid(from_id);
//		User toUser =userDao.getUserByUserid(to_id);
//
//		followDao.deleteByfromuserUseridAndTouserUserid(fromUser.getUserid(), toUser.getUserid());
//
//		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
//	}
//	
//	@GetMapping("/select_following/{from_id}")
//	@ApiOperation(value = "내가 팔로우하는 사람들")
//	public ResponseEntity<List<Follow>> select_following(@PathVariable int from_id) {
//		
//		List<Follow> list = followDao.findFollowByfromuserUserid(from_id);
//		
//		return new ResponseEntity<>(list, HttpStatus.OK);
//		
//	}
//	
//	@GetMapping("/select_follower/{to_id}")
//	@ApiOperation(value = "나를 팔로우하는 사람들")
//	public ResponseEntity<List<Follow>> select_follower(@PathVariable int to_id) {
//		
//		List<Follow> list = followDao.findFollowByTouserUserid(to_id);
//		
//		return new ResponseEntity<>(list, HttpStatus.OK);
//		
//	}
//	
////	@GetMapping("/select_detail")
////	@ApiOperation(value = "나를 팔로우하는 사람들 상세 조회")
////	public ResponseEntity<List<User>> select_detail(@RequestBody Set<Integer> follow) {
////		
////		List<Integer> list = new ArrayList<>(follow);
////		List<User> targetList = userDao.findAllUserByUseridIn(list);
////		
////		return new ResponseEntity<>(targetList, HttpStatus.OK);
////		
////	}
//}
