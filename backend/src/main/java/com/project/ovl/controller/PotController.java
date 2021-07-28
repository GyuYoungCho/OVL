package com.project.ovl.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PotDao;
import com.project.ovl.dao.PotRelationDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.pot.Pot;
import com.project.ovl.model.pot.PotRelation;
import com.project.ovl.model.pot.PotRequest;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pot")
@CrossOrigin("*")
public class PotController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";	
	
	@Autowired
    PotDao potDao;
	
	@Autowired
	PotRelationDao potrelationDao;
	
	@Autowired
    UserDao userDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "pot 등록")
	public ResponseEntity<String> regist(@RequestBody Pot pot){
		User user = userDao.getUserByUserid(1);
		Pot savePot = new Pot(0, pot.getTitle(), pot.getPlace(), 
				pot.getStep(), pot.getTime(), pot.getTotal_people(),
				pot.getRestaurant_name(),
				 pot.getType(),pot.getContent(),user);
		potDao.save(savePot);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "pot 수정")
	public ResponseEntity<String> modify(@PathVariable int potid){
		try {
			Pot isPot = potDao.getPotByPotid(potid);
			
			return null;
		} catch (Exception e){
			return null;
		}

	}
	
	@DeleteMapping("/delete/{potid}")
	@ApiOperation(value = "pot 삭제", response = String.class)
	public ResponseEntity<String> delete(@PathVariable int potid){
		Pot pot = potDao.getPotByPotid(potid);
		if(pot != null) {
			potDao.delete(pot);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/attend/{potid}/{userid}")
	@ApiOperation(value = "pot 참여")
	public ResponseEntity<String> attend(@PathVariable int potid, @PathVariable int userid){
		
		{
			
		}
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	
	@GetMapping("/select_all")
	@ApiOperation(value = "전체 팟 조회")
	public ResponseEntity<List<Pot>> select_all(){
		
		List<Pot> allPots = new ArrayList<>();
		if(!allPots.isEmpty()){
			allPots.addAll(potDao.findAll());
			return new ResponseEntity<>(allPots, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/select/{userid}")
	@ApiOperation(value = "내가 진행 중인 팟 조회")
	public ResponseEntity<List<PotRelation>> select(@PathVariable int userid){
		
		List<PotRelation> ingPots = new ArrayList<>();
		if(!ingPots.isEmpty()){
			ingPots.add(potrelationDao.getPotRelationByuserid(userid));
			return new ResponseEntity<>(ingPots, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	

	
}
