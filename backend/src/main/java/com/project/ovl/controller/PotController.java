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
	public ResponseEntity<String> regist(@RequestBody Pot pot, @RequestParam int userid){
		User user = userDao.getUserByUserid(userid);
		Pot savePot = new Pot(0, pot.getTitle(), pot.getPlace(), 
				pot.getStep(), pot.getTime(), pot.getTotal_people(),
				pot.getRestaurant_name(),
				 pot.getType(),pot.getContent(),user);
		potDao.save(savePot);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "pot 수정")
	public ResponseEntity<String> modify(@RequestBody Pot pot){
		Pot isPot = potDao.getPotByPotid(pot.getPotid());
		isPot.setTitle(pot.getTitle());
		isPot.setPlace(pot.getPlace());
		isPot.setTime(pot.getTime());
		isPot.setContent(pot.getContent());
		isPot.setTotal_people(pot.getTotal_people());
		isPot.setRestaurant_name(pot.getRestaurant_name());
		isPot.setType(pot.getType());
		potDao.save(isPot);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{potid}")
	@ApiOperation(value = "pot 삭제")
	public ResponseEntity<String> delete(@PathVariable int potid){
		List<PotRelation> relations = potrelationDao.findAll();
		List<PotRelation> reresults = new ArrayList<>();
		
		List<Pot> pots = potDao.findAll();
		//먼저 pot relation 삭제 후 가능
		for(PotRelation pr : relations) {
			if(pr.getPotid().getPotid()==potid) {
				potrelationDao.delete(pr);
			}
		}
		if(!pots.isEmpty()) {
			for(Pot plist : pots) {
				if(plist.getPotid() == potid)
					potDao.delete(plist);
			}
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/attend/{potid}/{userid}")
	@ApiOperation(value = "pot 참여")
	public ResponseEntity<String> attend(@PathVariable int potid, @PathVariable int userid){
		User user = userDao.getUserByUserid(userid);
		Pot pot = potDao.getPotByPotid(potid);
		//중복 등록 불가 하게 막기
		List<PotRelation> dpr = potrelationDao.findAll();
		List<PotRelation> existpots = new ArrayList<>();
		//전체 relation 돌면서 등록할때 이미 등록된 내용일 경우 목록을 가져온다.
		for(PotRelation pr : dpr) {
			if(pr.getPotid()==pot && pr.getUserid() == user) {
				existpots.add(pr);
				System.out.println(existpots + ": existpots 목록");
			}
		}
		//중복된 것이 없을 경우 등록 가능
		if(!existpots.isEmpty()) return new ResponseEntity<String>(FAIL, HttpStatus.FAILED_DEPENDENCY);
		else {	
			PotRelation potrelation = new PotRelation(0, pot, user);
			potrelationDao.save(potrelation);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/select_all")
	@ApiOperation(value = "전체 팟 조회", response = Pot.class)
	public ResponseEntity<List<Pot>> select_all(){
		
		List<Pot> allPots = potDao.findAll();
		if(!allPots.isEmpty()){
			return new ResponseEntity<List<Pot>>(allPots, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/select/{userid}")
	@ApiOperation(value = "내가 진행 중인 팟 조회")
	public ResponseEntity<List<Pot>> select(@PathVariable int userid){
		//현재 참여자가 있는 모든 팟 가져오기
		List<PotRelation> ingPots = potrelationDao.findAll();
		//현재 진행중인 팟에서 id에 해당하는 pot을 가져오기
		List<Pot> attendlist = new ArrayList<>();
		
		for(PotRelation pr : ingPots) {
			if(pr.getUserid().getUserid() == userid) {
				Pot result = potDao.getPotByPotid(pr.getPotid().getPotid());
				attendlist.add(result);
				//System.out.println(attendlist + ": potrelation 값 조회");
			}
		}
		if(!attendlist.isEmpty()) {
			//System.out.println("비어있지 않음");
			return new ResponseEntity<>(attendlist, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/attendCount/{potid}")
	@ApiOperation(value = "해당 팟 참여 유저 목록조회")
	public ResponseEntity<List<User>> attendCount(@PathVariable int potid){
		
		List<User> attendlist = new ArrayList<>();
		//현재 참여자가 있는 모든 팟 가져오기
		List<PotRelation> ingPots = potrelationDao.findAll();
		List<PotRelation> list = new ArrayList<>();
		//현재 진행중인 팟에서 potid에 해당하는 user를 가져오기

		
		for(PotRelation pr : ingPots) {
			if(pr.getPotid().getPotid() == potid) {

				list.add(pr);
				//System.out.println(list + ": pot 참여자가 있는 해당 팟");
			}
		}
		if(!list.isEmpty()) {
			//System.out.println("비어있지 않음");
			for(PotRelation pl : list) {
				User userlist = userDao.getUserByUserid(pl.getUserid().getUserid());
				attendlist.add(userlist);
			}
			return new ResponseEntity<>(attendlist, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	
	

	
}
