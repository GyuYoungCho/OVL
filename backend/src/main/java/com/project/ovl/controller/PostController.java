package com.project.ovl.controller;

import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.post.Post;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
	private static final String SUCCESS = "success";
	
	@Autowired
	PostDao postDao;
	
	@Autowired
	UserDao userDao;
	
	@PostMapping("/regist")
	@ApiOperation(value = "게시글 등록")
	public ResponseEntity<String> regist(@RequestBody Post post) {
		System.out.println("post : "+post);
		post.setTime(new Date());
		postDao.save(post);
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "게시글 수정")
	public ResponseEntity<String> modify(@RequestBody Post post) {
		post.setTime(new Date());
		postDao.save(post);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@DeleteMapping("/delete/{post_id}")
	@ApiOperation(value = "게시글 삭제")
	public ResponseEntity<String> delete(@PathVariable int post_id) {
		Post deletePost = postDao.findPostByPostId(post_id);
		postDao.delete(deletePost);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/select_all/{user_id}")
	@ApiOperation(value = "게시글 조회")
	public ResponseEntity<List<Post>> select_all(@PathVariable int user_id) {
		System.out.println("user_id : "+user_id);
		
		// 팔로우 controller 완성 해야댐~~
		List<Post> list = postDao.findAll();
		System.out.println("listCnt : "+list.size());
		return new ResponseEntity<List<Post>>(list, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail/{post_id}")
	@ApiOperation(value = "게시글 상세조회")
	public ResponseEntity<Post> select_detail(@PathVariable int post_id) {
		Post detailPost = postDao.findPostByPostId(post_id);
		return new ResponseEntity<Post>(detailPost, HttpStatus.OK);
	} 
}
