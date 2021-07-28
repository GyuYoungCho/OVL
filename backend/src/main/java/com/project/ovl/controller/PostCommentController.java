package com.project.ovl.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.report.Report;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/post/comment")
@CrossOrigin("*")
public class PostCommentController {
//	private static final String SUCCESS = "success";
//	private static final String FAIL = "fail";
//	
//	@Autowired
//	PostCommentDao postCommentDao;
//	
//	@PostMapping("/regist/{user_id}/{post_id}")
//	@ApiOperation(value = "댓글 등록")
//	public ResponseEntity<String> regist(@PathVariable int user_id, @PathVariable int report_id) {
//		User toUser = userDao.getUserByUserid(report_id);
//		User fromUser = userDao.getUserByUserid(user_id);
//		
//		// 신고당한 user warning+1
//		toUser.setWarning(toUser.getWarning()+1);
//		
//		// Report 테이블에 추가
//		Report newReport = new Report(0, fromUser, toUser);
//		postCommentDao.save(newReport);		
//		
//		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
//	}
//	
//	@GetMapping("/search_all/{post_id}")
//	@ApiOperation(value = "포스트의 모든 댓글 조회")
//	public ResponseEntity<Set<Integer>> select_all(@PathVariable int post_id) {
//		List<PostComment> list = postCommentDao.findAll();
//		Set<Integer> reportList = new HashSet<>();
//		for (Report r : list) {
//			if (r.getFromId().getUserid() == user_id) reportList.add(r.getToId().getUserid());
//		}
//		
//		return new ResponseEntity<Set<Integer>>(reportList, HttpStatus.OK);
//	}
//	
//	@PutMapping("/modify/{comment_id}")
//	@ApiOperation(value = "댓글 수정")
//	public ResponseEntity<String> modify(@PathVariable int comment_id) {
//		List<Report> list = reportDao.findAll();
//		Set<Integer> reportList = new HashSet<>();
//		for (Report r : list) {
//			if (r.getFromId().getUserid() == user_id) reportList.add(r.getToId().getUserid());
//		}
//		
//		return new ResponseEntity<Set<Integer>>(reportList, HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/unreport/{comment_id}")
//	@ApiOperation(value = "댓글 삭제")
//	public ResponseEntity<String> unreport(@PathVariable int comment_id) {
//		PostComment pc = postCommentDao.getByPostcommentid(comment_id);
//		postCommentDao.delete(pc);
//		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
//	}
//	
//	@GetMapping("/like_list/{user_id}")
//	@ApiOperation(value = "포스트의 모든 댓글 조회")
//	public ResponseEntity<Boolean> like_list(@PathVariable int user_id) {
//		List<Report> list = reportDao.findAll();
//		Set<Integer> reportList = new HashSet<>();
//		for (Report r : list) {
//			if (r.getFromId().getUserid() == user_id) reportList.add(r.getToId().getUserid());
//		}
//		
//		return new ResponseEntity<>(reportList, HttpStatus.OK);
//	}
//	
//	@GetMapping("/search_all/{post_id}")
//	@ApiOperation(value = "포스트의 모든 댓글 조회")
//	public ResponseEntity<Boolean> like(@PathVariable int user_id) {
//		List<Report> list = reportDao.findAll();
//		Set<Integer> reportList = new HashSet<>();
//		for (Report r : list) {
//			if (r.getFromId().getUserid() == user_id) reportList.add(r.getToId().getUserid());
//		}
//		
//		return new ResponseEntity<>(reportList, HttpStatus.OK);
//	}
	
}
