package com.project.ovl.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import com.project.ovl.dao.UserDao;
import com.project.ovl.dto.UserDto;
import com.project.ovl.model.jwt.JwtService;
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
	private JwtService jwtService;
	
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
	
	@GetMapping("/email_auth/{type}/{email}")
	@ApiOperation(value = "이메일 인증")
	public ResponseEntity<String> join_auth(@PathVariable String type, @PathVariable String email, HttpServletRequest request) throws UnsupportedEncodingException {
		User isEmail = userDao.getUserByEmail(email);
		if (isEmail==null) {
	    	HttpSession session = request.getSession();
	    	int authorization = mService.mailSend(email, type);
	    	session.setAttribute(email, authorization);
	    	System.out.println("join_auth email : "+email+", session : "+session.getAttribute(email));
			if (authorization!=0) return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/email_auth_check/{email}/{confirm}")
	@ApiOperation(value = "이메일 인증번호 확인")
	public ResponseEntity<String> join_auth_check( @PathVariable String email, @PathVariable String confirm, HttpSession session) throws UnsupportedEncodingException {
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
	
	@PutMapping("/modify_pw/{email}/{password}")
	@ApiOperation(value = "비밀번호 변경")
	public ResponseEntity<String> modify_pw(@PathVariable String email, @PathVariable String password){
		try {
			User isEmail = userDao.getUserByEmail(email);
			isEmail.setPassword(password);
			userDao.save(isEmail);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/join")
	@ApiOperation(value = "회원가입")
	public ResponseEntity<String> join(@Valid @RequestBody SignupRequest request){
		User saveUser = new User(0, request.getEmail(), request.getNickname(), request.getName(), request.getPhone(),
<<<<<<< HEAD
				 request.getPassword(), request.getExperience(), request.getAccount_open(), request.getWarning(),null,null);
=======
				 request.getPassword(), request.getExperience(), request.getAccount_open(), request.getWarning(), null,null,null);
>>>>>>> 590773aefb51e345ba14c9dab9ce93113200fc5a
		userDao.save(saveUser);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);

	}
	
	@Transactional
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto loginDto, HttpServletResponse res) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		ResponseEntity<Map<String, Object>> entity = null;
		String token = "";
		
		try {
			User userlogin = userDao.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword()).get();
			token = jwtService.create(userlogin);
			
			resultMap.putAll(jwtService.get(token));
			
			resultMap.put("status", true);
			resultMap.put("data", userlogin);
			resultMap.put("token", token);
			
			entity = ResponseEntity.accepted().header("access-token", token).body(resultMap);
			
		}catch(RuntimeException e){
			//Logger.info("로그인 실패",e);
			resultMap.put("message", e.getMessage());
			entity = ResponseEntity.badRequest().body(resultMap);
		}
		return entity;
		
	}
	
	@ApiOperation(value = "name, phone으로 이메일 찾기. 'success' or 'fail'", response = String.class)
	@GetMapping("/search_id/{name}/{phone}")
	public ResponseEntity<String> search_id(@PathVariable("name") String name,
											@PathVariable("phone") String phone) {
		Optional<User> userOpt = userDao.findUserByNameAndPhone(name, phone);
		if (userOpt.isPresent()) {
            return new ResponseEntity<>(userOpt.get().getEmail(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
        }
	}
	
	@ApiOperation(value = "회원 조회", response = User.class)
	@GetMapping("/select/{user_id}")
	public ResponseEntity<User> select(@PathVariable int user_id) {
		User user = userDao.getUserByUserid(user_id);
		if (user!=null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
	}

    @ApiOperation(value = "회원 수정", response = String.class)
	@PutMapping("/modify_user")
	public ResponseEntity<String> search_id(@RequestBody User user, @RequestPart("picture") MultipartFile pic) {
    	User useropt = userDao.getUserByUserid(user.getUserid());
    	useropt.setNickname(user.getNickname());
    	useropt.setExperience(user.getExperience());
    	useropt.setPhone(user.getPhone());
		userDao.save(useropt);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
    
    @ApiOperation(value = "회원 탈퇴", response = String.class)
	@DeleteMapping("/delete/{user_id}")
	public ResponseEntity<String> delete(@PathVariable int user_id) {
    	User user = userDao.getUserByUserid(user_id);
		if(user!=null) {
			userDao.delete(user);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

    
}
