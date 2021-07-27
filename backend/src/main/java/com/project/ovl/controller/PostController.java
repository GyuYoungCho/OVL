package com.project.ovl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.PostLIkeDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.user.User;

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
	
	@Autowired
    FollowDao followDao;
	
	@Autowired
    PostLIkeDao postLikeDao;
	
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
		// 내가 팔로우 한 사람 찾기
		Optional<List<Follow>> followList = followDao.findByFromIdUserid(user_id);
		Set<Integer> followingList = new HashSet<>();
		if (followList.isPresent()) {
			for (Follow r : followList.get()) {
				followingList.add(r.getToId().getUserid());
			}
		}
		
		// 내가 팔로우 한 사람의 게시글만 저장해서 보내주기
		List<Post> postList = postDao.findAll();
		List<Post> returnList = new ArrayList<>();
		for (Post p : postList) {
			if (followingList.contains(p.getUserId().getUserid()) || p.getUserId().getUserid()==user_id) returnList.add(p);
		}
		
		return new ResponseEntity<List<Post>>(returnList, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail/{post_id}")
	@ApiOperation(value = "게시글 상세조회")
	public ResponseEntity<Post> select_detail(@PathVariable int post_id) {
		Post detailPost = postDao.findPostByPostId(post_id);
		return new ResponseEntity<Post>(detailPost, HttpStatus.OK);
	} 
	
	@GetMapping("/like/{user_id}/{post_id}")
	@ApiOperation(value = "좋아요 누르기 or 취소")
	public ResponseEntity<String> like(@PathVariable int user_id, @PathVariable int post_id) {
		User user = userDao.getUserByUserid(user_id);
		Post post = postDao.findPostByPostId(post_id);
		PostLike like = postLikeDao.findPostLikeByUserIdAndPostId(user, post);
		 
		// like 테이블에 존재하는지 확인
		if (like==null) { // 존재하지 않을 시
			// 해당 post like_count+1
			post.setLike_count(post.getLike_count()+1);
			
			// like 테이블에 저장
			postLikeDao.save(new PostLike(0, user, post));
		} else { // 이미 존재 시
			// 해당 post like_count-1
			post.setLike_count(post.getLike_count()-1);
			
			postLikeDao.delete(like);
		}
		postDao.save(post);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/like_list/{user_id}")
	@ApiOperation(value = "내가 좋아요 누른 게시글 목록")
	public ResponseEntity<Set<Integer>> like_list(@PathVariable int user_id) {
		// 모든 좋아요 데이터 가져오기
		List<PostLike> likeList = postLikeDao.findAll();
		Set<Integer> returnSet = new HashSet<>();
		
		// 좋아요 데이터에 있는 user_id와 나의 user_id가 일치할 시 post_id를 set에 저장
		for (PostLike pl : likeList) {
			if (pl.getUserId().getUserid()==user_id) returnSet.add(pl.getPostId().getPostId());
		}
		return new ResponseEntity<Set<Integer>>(returnSet, HttpStatus.OK);
	}
}
