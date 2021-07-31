package com.project.ovl.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.PostCommentDao;
import com.project.ovl.dao.PostDao;
import com.project.ovl.dao.PostLIkeDao;
import com.project.ovl.dao.PostPhotoDao;
import com.project.ovl.dao.UserDao;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.photo.PhotoHandler;
import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
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
	
	@Autowired
	PostPhotoDao postPhotoDao;
	
	@Autowired
	PostCommentDao postCommentDao;
	
	@Autowired
	PostCommentController commentController;
	
	@Autowired
	PhotoHandler photoHandler;
	
	@PostMapping("/regist")
	@ApiOperation(value = "게시글 등록")
	public ResponseEntity<String> regist(@RequestPart("files") List<MultipartFile> files, @RequestPart("categori") String categori,
											@RequestPart("content") String content, @RequestPart("userId") String userId) throws Exception {
		// 게시글 저장
		User user = userDao.getUserByUserid(Integer.parseInt(userId));
		// 게시글 등록 시 유저에 경험치 5 추가
		int value = 0;
		if (Integer.parseInt(categori)==1) value = 5;
		else if (Integer.parseInt(categori)==2) value = 3;
		else value = 2;
		
		user.setExperience(user.getExperience()+value);
		userDao.save(user);
		
		// 게시글 등록
		Post post = new Post(0, Integer.parseInt(categori), content, 0, 0, new Date(), user);
		postDao.save(post);
		// 이미지 저장
		List<PostPhoto> photoList = photoHandler.parseFileInfo(files, post.getPostId());
		
		for (PostPhoto pp : photoList) {
			postPhotoDao.save(pp);
		}
		
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value = "게시글 수정")
	public ResponseEntity<String> modify(@RequestPart("files") List<MultipartFile> files, @RequestPart("categori") int categori,
			@RequestPart("content") String content, @RequestPart("postId") int postId) throws Exception {
		Post post = postDao.findPostByPostId(postId);
		post.setCategori(categori);
		post.setContent(content);
		
		postDao.save(post);
		
		// 원래 있던 사진 다 지우기
		List<PostPhoto> deleteList = postPhotoDao.findAll();
		for (PostPhoto pp : deleteList) {
			if (pp.getPostId().getPostId()==postId) postPhotoDao.delete(pp);
		}
		
		// 사진 다시 저장
		List<PostPhoto> photoList = photoHandler.parseFileInfo(files, post.getPostId());

		for (PostPhoto pp : photoList) {
			postPhotoDao.save(pp);
		}
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@DeleteMapping("/delete/{post_id}")
	@ApiOperation(value = "게시글 삭제")
	public ResponseEntity<String> delete(@PathVariable int post_id) {
		// 해당 게시글 올린 유저 경험치 -5
		Post post = postDao.findPostByPostId(post_id);
		
		int value = 0;
		if (post.getCategori()==1) value = 5;
		else if (post.getCategori()==2) value = 3;
		else value = 2;
		
		User user = userDao.getUserByUserid(post.getUserId().getUserid());
		user.setExperience(user.getExperience()-value);
		userDao.save(user);
		
		// 해당 게시글 좋아요 리스트 삭제
		List<PostLike> likeList = postLikeDao.findAll();
		
		for (PostLike pl : likeList) {
			if (pl.getPostId().getPostId()==post_id) postLikeDao.delete(pl);
		}
		
		// 해당 게시글 사진 리스트 삭제
		List<PostPhoto> photoList = postPhotoDao.findAll();
		
		for (PostPhoto pp : photoList) {
			if (pp.getPostId().getPostId()==post_id) postPhotoDao.delete(pp);
		}
		
		// 해당 게시글 댓글 삭제
		List<PostComment> commentList = postCommentDao.findAll();
		
		for (PostComment pc : commentList) {
			if (pc.getPostId().getPostId()==post_id) commentController.delete(pc.getPostCommentId());
		}
		
		postDao.delete(post);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	} 
	
	@GetMapping("/select_all/{user_id}")
	@ApiOperation(value = "게시글 조회")
	public ResponseEntity<List<PostPhoto>> select_all(@PathVariable int user_id) {
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
		// 해당 게시글의 이미지 리스트를 찾기 위해 이미지 데이터 싹 가져오기
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		for (Post p : postList) {
			if (followingList.contains(p.getUserId().getUserid()) || p.getUserId().getUserid() == user_id) {
				List<PostPhoto> saveList = new ArrayList<>();
				// 해당 게시물의 이미지를 리스트에 저장
				for (PostPhoto pp : photoList) {
					if (pp.getPostId().getPostId()==p.getPostId()) saveList.add(pp);
				}
				
				Collections.sort(saveList, (o1, o2)-> {
					return Integer.compare(o1.getPostPhotoId(), o2.getPostPhotoId());
				});
				
				returnList.add(saveList.get(0));
			}
		}
		// 최신글을 먼저 보여줘야 하므로 post_id 역순으로 정렬 
		Collections.sort(returnList, (o1, o2)-> {
			return Integer.compare(o2.getPostPhotoId(), o1.getPostPhotoId());
		});
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
	} 

	@GetMapping("/select_user/{user_id}")
	@ApiOperation(value = "자기 자신 게시글 조회")
	public ResponseEntity<List<PostPhoto>> select_user(@PathVariable int user_id) {
		// 내가 작성한 게시글만 저장해서 보내주기
		List<Post> postList = postDao.findAll();
		// 해당 게시글의 이미지 리스트를 찾기 위해 이미지 데이터 싹 가져오기
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		for (Post p : postList) {
			if (p.getUserId().getUserid() == user_id) {
				List<PostPhoto> saveList = new ArrayList<>();
				// 해당 게시물의 이미지를 리스트에 저장
				for (PostPhoto pp : photoList) {
					if (pp.getPostId().getPostId()==p.getPostId()) saveList.add(pp);
				}
				
				Collections.sort(saveList, (o1, o2)-> {
					return Integer.compare(o1.getPostPhotoId(), o2.getPostPhotoId());
				});
				
				returnList.add(saveList.get(0));
			}
		}
		// 최신글을 먼저 보여줘야 하므로 post_id 역순으로 정렬 
		Collections.sort(returnList, (o1, o2)-> {
			return Integer.compare(o2.getPostPhotoId(), o1.getPostPhotoId());
		});
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail/{post_id}")
	@ApiOperation(value = "게시글 상세조회")
	public ResponseEntity<Post> select_detail(@PathVariable int post_id) {
		Post detailPost = postDao.findPostByPostId(post_id);
		return new ResponseEntity<Post>(detailPost, HttpStatus.OK);
	} 
	
	@GetMapping("/select_detail_photo/{post_id}")
	@ApiOperation(value = "게시글 이미지 상세조회")
	public ResponseEntity<List<PostPhoto>> select_detail_photo(@PathVariable int post_id) {
		List<PostPhoto> photoList = postPhotoDao.findAll();
		List<PostPhoto> returnList = new ArrayList<>();
		
		for (PostPhoto pl : photoList) {
			if (pl.getPostId().getPostId()==post_id) returnList.add(pl);
		}
		
		return new ResponseEntity<List<PostPhoto>>(returnList, HttpStatus.OK);
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
