package com.project.ovl.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.report.Report;
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
	
	
	@PostMapping("/follow/{from_id}/{to_id}")
	@ApiOperation(value = "팔로우하기")
	public ResponseEntity<String> follow(@PathVariable int from_id, @PathVariable int to_id) {
		User fromUser = userDao.getUserByUserid(from_id);
		User toUser =userDao.getUserByUserid(to_id);

		Follow follow = new Follow();
		follow.setFromId(fromUser);
		follow.setToId(toUser);

		followDao.save(follow);

		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}

	@DeleteMapping("/follow/{from_id}/{to_id}")
	@ApiOperation(value = "언팔하기")
	public ResponseEntity<String> unFollow(@PathVariable int from_id, @PathVariable int to_id) {
		User fromUser = userDao.getUserByUserid(from_id);
		User toUser =userDao.getUserByUserid(to_id);

		followDao.deleteByfromIdUseridAndToIdUserid(fromUser.getUserid(), toUser.getUserid());

		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/select_following/{from_id}")
	@ApiOperation(value = "내가 팔로우하는 사람들")
	public ResponseEntity<Set<Integer>> select_following(@PathVariable int from_id) {
		Optional<List<Follow>> list = followDao.findByFromIdUserid(from_id);
		
		if (list.isPresent()) {
			Set<Integer> followingList = new HashSet<>();
			for (Follow r : list.get()) {
				followingList.add(r.getToId().getUserid());
			}
			return new ResponseEntity<>(followingList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
	}
	
	@GetMapping("/select_follower/{to_id}")
	@ApiOperation(value = "나를 팔로우하는 사람들")
	public ResponseEntity<Set<Integer>> select_follower(@PathVariable int to_id) {
		
		Optional<List<Follow>> list = followDao.findByToIdUserid(to_id);
		
		if (list.isPresent()) {
			Set<Integer> followerList = new HashSet<>();
			for (Follow r : list.get()) {
				followerList.add(r.getFromId().getUserid());
			}
			return new ResponseEntity<>(followerList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
		
	}
	
	@PostMapping("/select_detail")
	@ApiOperation(value = "나를 팔로우하는 사람들 상세 조회")
	public ResponseEntity<List<User>> select_detail(@RequestBody List<Integer> follow) {
		
		List<User> targetList = new ArrayList<>();
		
		if (!follow.isEmpty()) {
			for (int u : follow) {
				targetList.add(userDao.getUserByUserid(u));
			}
			return new ResponseEntity<>(targetList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
		
	}
}
