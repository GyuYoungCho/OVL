package com.project.ovl.controller.feed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.post.PostDao;
import com.project.ovl.dao.post.PostPhotoDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/feed")
@CrossOrigin("*")
public class FeedController {
	
	@Autowired
	PostPhotoDao postPhotoDao;
	
	@Autowired
	FollowDao followDao;
	
	@Autowired
	UserDao userDao;
	
	@GetMapping("/postfeed")
	@ApiOperation(value = "뉴스 피드 페이징")
	public Page<PostPhoto> feedmain(@RequestParam("userId") int userId, final Pageable pageable) {
		
		Page<PostPhoto> postpage = postPhotoDao.findByUserFollowing(userId, pageable);
		List<PostPhoto> posts = postpage.toList();
		return new PageImpl<PostPhoto>(posts,pageable,postpage.getTotalElements());
	}
	
	@GetMapping("/popularfeed")
	@ApiOperation(value = "초반이라 팔로우 등 없을 때")
	public Page<PostPhoto> popularfeed(final Pageable pageable) {
		Pageable sortedByTimeDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
				Sort.by("time").descending().and(Sort.by("like"))); 
		Page<PostPhoto> postpage = postPhotoDao.findAll(pageable);
		List<PostPhoto> posts = postpage.toList();
		return new PageImpl<PostPhoto>(posts,sortedByTimeDesc,postpage.getTotalElements());
	}
	
	@GetMapping("/postfeed/search")
	@ApiOperation(value = "포스트 검색")
	public Page<PostPhoto> searchpostkeyword(@RequestParam(required = false, defaultValue = "") String keyword,
			Pageable pageable) {
		// 페이지 index는 0부터
		keyword = "%" + keyword + "%";
		Page<PostPhoto> postpage = postPhotoDao.findByContentkeyword(keyword, pageable);
		List<PostPhoto> posts = postpage.toList();
		return new PageImpl<PostPhoto>(posts, pageable, postpage.getTotalElements());
	}
	
	@ApiOperation(value = "모든 회원 조회")
	@GetMapping("/user/select_all")
	public Page<User> select_all(@RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable) {
		keyword = "%" + keyword + "%";
		Pageable sortedByExpDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("experience").descending()); 
		Page<User> userpage = userDao.findAll(sortedByExpDesc);
		List<User> userlist = userpage.toList();
		return new PageImpl<User>(userlist, sortedByExpDesc, userpage.getTotalElements());
	}

//	@GetMapping("/following/page/{from_id}")
//	@ApiOperation(value = "내가 팔로우하는 스크롤")
//	public Page<User> findfollowing(@RequestParam int from_id, final Pageable pageable) {
//		Page<User> flist = followDao.findfollwing(from_id, pageable);
//		List<User> ulist = flist.toList();
//		return new PageImpl<User>(ulist,pageable,flist.getTotalElements());
//	}
//	
//	@GetMapping("/follower/page/{to_id}")
//	@ApiOperation(value = "나를 팔로우하는 사람들 스크롤")
//	public Page<User> findfollower(@PathVariable int to_id, final Pageable pageable) {
//		
//		Page<User> flist = followDao.findfollwer(to_id, pageable);
//		List<User> ulist = flist.toList();
//		return new PageImpl<User>(ulist,pageable,flist.getTotalElements());
//		
//	}
}
