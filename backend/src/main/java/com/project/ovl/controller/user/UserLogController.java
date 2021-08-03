package com.project.ovl.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.user.UserDao;
import com.project.ovl.dao.user.UserLogDao;
import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/userlog")
@CrossOrigin("*")
public class UserLogController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
    UserLogDao userLogDao;
	
	@ApiOperation(value = "유저 기록 조희")
	@GetMapping("/select/{user_id}")
	public ResponseEntity<List<UserLog>> select(@PathVariable int user_id) {
		User user = userDao.getUserByUserid(user_id);
		List<UserLog> list = userLogDao.findTop300ByUserIdOrderByLogDateDesc(user);
		if (user!=null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
	}
}
