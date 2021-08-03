package com.project.ovl.controller.challenge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.challenge.ChallengeDao;
import com.project.ovl.dao.challenge.ChallengeHistoryDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.dao.user.UserLogDao;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeHistory;
import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/challenge")
@CrossOrigin("*")
public class ChallengeController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	ChallengeDao challengeDao;
	
	@Autowired
	ChallengeHistoryDao challengeHistoryDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserLogDao userLogDao;
	
	@GetMapping("/attend")
	@ApiOperation(value = "챌린지 참여스")
	public ResponseEntity<String> attend(@RequestParam("challenge_id") int challenge_id,
			@RequestParam("user_id") int user_id) {
		
		User user = userDao.getUserByUserid(user_id);		
		// 참여 챌린지 없을 경우 등록 
		if(user.getChallengeId().getChallengeId()==1) {
			Challenge challenge = challengeDao.findByChallengeId(challenge_id);
			user.setChallengeId(challenge);
			userDao.save(user);
			return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/delete/{user_id}")
	@ApiOperation(value = "사용자가 챌린지 참여 중단")
	public ResponseEntity<String> delete(@PathVariable int user_id) {
		User user = userDao.getUserByUserid(user_id);
		
		Challenge challenge = challengeDao.findByChallengeId(1);
		user.setChallengeId(challenge);
		userDao.save(user);
		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/complete/{use_id}")
	@ApiOperation(value = "사용자가 챌린지 완료")
	public ResponseEntity<String> complete(@PathVariable int user_id,@PathVariable int div) {
		User user = userDao.getUserByUserid(user_id);
		Challenge endChallenge = user.getChallengeId();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endChallenge.getStart_date());
		cal.add(Calendar.DATE, endChallenge.getPeriod());
		
		//완료된 챌린지 저장
		challengeHistoryDao.save(new ChallengeHistory(0, endChallenge, user,cal.getTime()));
		
		// 경험치 획득 및 챌린지 없는 상태로 만들기
		user.setExperience(user.getExperience() + endChallenge.getScore()/div);
		user.setChallengeId(challengeDao.findByChallengeId(1));
		userDao.save(user);
		
		userLogDao.save(new UserLog(0, user, new Date(), 3, 1, 
				endChallenge.getChallengeId(), endChallenge.getScore()));
		return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/search_all")
	@ApiOperation(value = "모든 챌린지 보기")
	public ResponseEntity<List<Challenge>> search_all() {
		
		List<Challenge> list = challengeDao.findAll();
		if(!list.isEmpty()) {
			List<Challenge> resultSet = new ArrayList<>();
			for(Challenge ch : list) {
				if(ch.getChallengeId()!=1) resultSet.add(ch);
			}
			return new ResponseEntity<>(resultSet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping("/search_detail/{challenge_id}")
	@ApiOperation(value = "챌린지 상세 조회")
	public ResponseEntity<Challenge> search_detail(@PathVariable int challenge_id) {
		Challenge challenge = challengeDao.findByChallengeId(challenge_id);
		
		return new ResponseEntity<>(challenge, HttpStatus.OK);
	}
	
	@GetMapping("/attend_list/{challenge_id}")
	@ApiOperation(value = "챌린지 참여 유저 목록")
	public ResponseEntity<List<User>> attend_list(@PathVariable int challenge_id) {
		System.out.println("hi");
		Optional<List<User>> list = userDao.findByChallengeIdChallengeId(challenge_id);
		if(list.isPresent()) {
			return new ResponseEntity<>(list.get(), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping("/search_history/{user_id}")
	@ApiOperation(value = "이전에 참여한 챌린지")
	public ResponseEntity<List<Challenge>> search_history(@PathVariable int user_id) {
		
		Optional<List<ChallengeHistory>> list = challengeHistoryDao.findByUserIdUserid(user_id);
		if(list.isPresent()) {
			List<Challenge> resultSet = new ArrayList<>();
			for(ChallengeHistory ch : list.get()) {
				resultSet.add(ch.getChallengeId());
			}
			return new ResponseEntity<>(resultSet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
}
