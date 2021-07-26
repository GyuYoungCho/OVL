package com.project.ovl.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.ReportDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.report.Report;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/report")
@CrossOrigin("*")
public class ReportController {
	private static final String SUCCESS = "success";
	
	@Autowired
    UserDao userDao;
	
	@Autowired
    ReportDao reportDao;
	
	@GetMapping("/regist/{user_id}/{report_id}")
	@ApiOperation(value = "신고 등록")
	public ResponseEntity<String> regist(@PathVariable int user_id, @PathVariable int report_id) {
		User toUser = userDao.getUserByUserid(report_id);
		User fromUser = userDao.getUserByUserid(user_id);
		
		// 신고당한 user warning+1
		toUser.setWarning(toUser.getWarning()+1);
		
		// Report 테이블에 추가
		Report newReport = new Report(0, fromUser, toUser);
		reportDao.save(newReport);		
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/select/{user_id}")
	@ApiOperation(value = "신고 조회")
	public ResponseEntity<Set<Integer>> select(@PathVariable int user_id) {
		List<Report> list = reportDao.findAll();
		Set<Integer> reportList = new HashSet<>();
		for (Report r : list) {
			if (r.getFromId().getUserid() == user_id) reportList.add(r.getToId().getUserid());
		}
		
		return new ResponseEntity<Set<Integer>>(reportList, HttpStatus.OK);
	}
	
	@GetMapping("/unreport/{user_id}/{report_id}")
	@ApiOperation(value = "신고 취소")
	public ResponseEntity<String> unreport(@PathVariable int user_id, @PathVariable int report_id) {
		User fromUser = userDao.getUserByUserid(user_id);
		User toUser = userDao.getUserByUserid(report_id);
		
		// 신고당한 user warning-1
		toUser.setWarning(toUser.getWarning()-1);
		userDao.save(toUser);
		
		// Report 테이블 삭제
		Report report = reportDao.findReportByFromIdAndToId(fromUser, toUser);
		reportDao.delete(report);
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
}
