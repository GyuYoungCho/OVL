package com.project.ovl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.pot.RestaurantDao;
import com.project.ovl.model.pot.Restaurant;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/restaurant")
@CrossOrigin("*")
public class RestaurantController {

	@Autowired
	RestaurantDao restaurantDao;
	
	@GetMapping("/select_all")
	@ApiOperation(value = "전체 팟 조회")
	public ResponseEntity<List<Restaurant>> select_all(){
		
		List<Restaurant> allres = restaurantDao.findAll();
		if(!allres.isEmpty()){
			return new ResponseEntity<>(allres, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

}
