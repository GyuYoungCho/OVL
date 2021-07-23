package com.project.ovl.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.project.ovl.dao.UserDao;
import com.project.ovl.model.mail.mailService;
import com.project.ovl.model.user.SignupRequest;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
    UserDao userDao;
	
	@Autowired
    mailService mService;
	
	@GetMapping("/nickname_check/{nickname}")
	@ApiOperation(value = "닉네임 중복 체크")
	public ResponseEntity<String> nickname_check(@PathVariable String nickname) {
		User isNickname = userDao.getUserByNickname(nickname);
		if (isNickname==null) return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		else return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/join_auth/{email}")
	@ApiOperation(value = "회원가입 이메일 인증")
	public ResponseEntity<String> join_auth(@PathVariable String email, HttpServletRequest request) throws UnsupportedEncodingException {
		User isEmail = userDao.getUserByEmail(email);
		if (isEmail==null) {
	    	HttpSession session = request.getSession();
	    	int authorization = mService.mailSend(email, "join");
	    	session.setAttribute(email, authorization);
	    	System.out.println("join_auth email : "+email+", session : "+session.getAttribute(email));
			if (authorization!=0) return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/join_auth_check/{email}/{confirm}")
	@ApiOperation(value = "회원가입 이메일 인증번호 확인")
	public ResponseEntity<String> join_auth_check(@PathVariable String email, @PathVariable String confirm, HttpSession session) throws UnsupportedEncodingException {
    	try {
    		int authorization = (int) session.getAttribute(email);
    		if (authorization>0) {
    			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    		} else {
    			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    		}
    	} catch(Exception e) {
    		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    	}
	}
	
	@PostMapping("/join")
	@ApiOperation(value = "회원가입")
	public ResponseEntity<String> join(@Valid @RequestBody SignupRequest request){
		User saveUser = new User(0, request.getEmail(), request.getNickname(), request.getName(), request.getPhone(),
				 request.getPassword(), request.getExperience(), request.getAccount_open(), request.getWarning(), null, null, null);
		userDao.save(saveUser);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
}
