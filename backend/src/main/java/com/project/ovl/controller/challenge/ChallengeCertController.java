package com.project.ovl.controller.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.challenge.ChallengeCertificationDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.user.User;

import java.util.List;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/challenge/cert")
@CrossOrigin("*")
public class ChallengeCertController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ChallengeCertificationDao challengeCertificationDao;
	
	@GetMapping("/search_list/{userid}")
	@ApiOperation(value = "유저의 챌린지 인증 정보")
	public ResponseEntity<List<ChallengeCertification>> search_detail(@PathVariable int userid) {
		User user = userDao.getUserByUserid(userid);
		List<ChallengeCertification> cc = challengeCertificationDao.findByUserId(user);
		
		return new ResponseEntity<>(cc, HttpStatus.OK);
	}
}
