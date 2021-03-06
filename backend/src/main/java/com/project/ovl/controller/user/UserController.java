package com.project.ovl.controller.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.controller.post.PostCommentController;
import com.project.ovl.controller.post.PostReplyController;
import com.project.ovl.dao.FollowDao;
import com.project.ovl.dao.ReportDao;
import com.project.ovl.dao.challenge.ChallengeCertificationDao;
import com.project.ovl.dao.challenge.ChallengeDao;
import com.project.ovl.dao.challenge.ChallengeHistoryDao;
import com.project.ovl.dao.post.PostCommentDao;
import com.project.ovl.dao.post.PostCommentLikeDao;
import com.project.ovl.dao.post.PostDao;
import com.project.ovl.dao.post.PostLIkeDao;
import com.project.ovl.dao.post.PostPhotoDao;
import com.project.ovl.dao.post.PostReplyDao;
import com.project.ovl.dao.post.PostReplyLikeDao;
import com.project.ovl.dao.pot.PotDao;
import com.project.ovl.dao.pot.PotRelationDao;
import com.project.ovl.dao.recipe.RecipeCommentDao;
import com.project.ovl.dao.recipe.RecipeCommentLikeDao;
import com.project.ovl.dao.recipe.RecipeDao;
import com.project.ovl.dao.recipe.RecipeLikeDao;
import com.project.ovl.dao.recipe.RecipeProcessDao;
import com.project.ovl.dao.recipe.RecipeReplyDao;
import com.project.ovl.dao.recipe.RecipeReplyLikeDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.dao.user.UserLogDao;
import com.project.ovl.dto.UserDto;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.challenge.ChallengeHistory;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.jwt.JwtService;
import com.project.ovl.model.like.PostCommentLike;
import com.project.ovl.model.like.PostLike;
import com.project.ovl.model.like.PostReplyLike;
import com.project.ovl.model.like.RecipeCommentLike;
import com.project.ovl.model.like.RecipeLike;
import com.project.ovl.model.like.RecipeReplyLike;
import com.project.ovl.model.mail.mailService;
import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.post.PostComment;
import com.project.ovl.model.post.PostReply;
import com.project.ovl.model.pot.Pot;
import com.project.ovl.model.pot.PotRelation;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.recipe.RecipeComment;
import com.project.ovl.model.recipe.RecipeProcess;
import com.project.ovl.model.recipe.RecipeReply;
import com.project.ovl.model.report.Report;
import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	private HttpSession session;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
    UserDao userDao;
	
	@Autowired
	ChallengeDao challengedao;
	
	@Autowired
    mailService mService;
	
	@Autowired
	PostReplyLikeDao postReplyLikeDao;
	
	@Autowired
	PostCommentLikeDao postCommentLikeDao;
	
	@Autowired
	PostLIkeDao postLikeDao;
	
	@Autowired
	PostReplyDao postReplyDao;
	
	@Autowired
	PostCommentDao postCommentDao;
	
	@Autowired
	PostPhotoDao postPhotoDao;
	
	@Autowired
	PostDao postDao;
	
	@Autowired
	RecipeReplyLikeDao recipeReplyLikeDao;
	
	@Autowired
	RecipeCommentLikeDao recipeCommentLikeDao;
	
	@Autowired
	RecipeLikeDao recipeLikeDao;
	
	@Autowired
	RecipeReplyDao recipeReplyDao;
	
	@Autowired
	RecipeCommentDao recipeCommentDao;
	
	@Autowired
	RecipeProcessDao recipeProcessDao;
	
	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	PotDao potDao;
	
	@Autowired
	PotRelationDao potRelationDao;
	
	@Autowired
	FollowDao followDao;
	
	@Autowired
	ReportDao reportDao;
	
	@Autowired
	ChallengeHistoryDao challengeHistoryDao;
	
	@Autowired
	ChallengeCertificationDao challengeCertificationDao;
	
	@Autowired
	UserLogDao userLogDao;
	
	@Autowired
	PostCommentController postCommentController;

	@Autowired
	PostReplyController postReplyController;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/nickname_check/{nickname}")
	@ApiOperation(value = "????????? ?????? ??????")
	public ResponseEntity<String> nickname_check(@PathVariable String nickname) {
		User isNickname = userDao.getUserByNickname(nickname);
		if (isNickname==null) return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		else return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/email_auth/{type}/{email}")
	@ApiOperation(value = "????????? ??????")
	public ResponseEntity<String> join_auth(@PathVariable String type, @PathVariable String email, HttpServletRequest request) throws UnsupportedEncodingException {
		User isEmail = userDao.getUserByEmail(email);
		if ((isEmail==null && type.equals("join")) || (isEmail!=null && type.equals("password"))) {
			session = request.getSession();
	    	int authorization = mService.mailSend(email, type);
	    	session.setAttribute(email, authorization);
			if (authorization!=0) return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} 
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/email_auth_check/{email}/{confirm}")
	@ApiOperation(value = "????????? ???????????? ??????")
	public ResponseEntity<String> join_auth_check(@PathVariable String email, @PathVariable String confirm) throws UnsupportedEncodingException {
    	try {
    		int authorization = (int) session.getAttribute(email);
    		if (authorization==Integer.parseInt(confirm)) {
    			session.setAttribute(email, null);
    			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    		} else {
    			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    		}
    	} catch(Exception e) {
    		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    	}
	}
	
	@PutMapping("/modify_pw/{email}/{password}")
	@ApiOperation(value = "???????????? ??????")
	public ResponseEntity<String> modify_pw(@PathVariable String email, @PathVariable String password){
		try {
			User isEmail = userDao.getUserByEmail(email);
			isEmail.setPassword(passwordEncoder.encode(password));
			userDao.save(isEmail);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/join")
	@ApiOperation(value = "????????????")
	public ResponseEntity<String> join(@RequestBody User user){
		Challenge basic = challengedao.findByChallengeId(1);
		User saveUser = new User(0, user.getEmail(), user.getNickname(), user.getName(), user.getPhone(),
				passwordEncoder.encode(user.getPassword()), 0, 0, 0, "https://ovl-bucket.s3.ap-northeast-2.amazonaws.com/defaultImg.jpg" ,basic);
		userDao.save(saveUser);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);

	}
	
	@Transactional
	@PostMapping("/login")
	@ApiOperation(value = "?????????")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto loginDto, HttpServletResponse res) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		ResponseEntity<Map<String, Object>> entity = null;
		String token = "";
		
		try {
			User userlogin = userDao.getUserByEmail(loginDto.getEmail());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(loginDto.getPassword(), userlogin.getPassword())) {
				token = jwtService.create(userlogin);
				
				resultMap.putAll(jwtService.get(token));
				
				resultMap.put("status", true);
				resultMap.put("data", userlogin);
				resultMap.put("token", token);
				
				entity = ResponseEntity.accepted().header("access-token", token).body(resultMap);
			} else {
				resultMap.put("message", "????????? ??????");
				entity = ResponseEntity.badRequest().body(resultMap);
			}
		}catch(RuntimeException e){
			resultMap.put("message", e.getMessage());
			entity = ResponseEntity.badRequest().body(resultMap);
		}
		return entity;
		
	}
	
	@Transactional
	@PostMapping("/info")
	@ApiOperation(value = "?????? ????????????")
	public ResponseEntity<Map<String, Object>> gettokenInfo(HttpServletRequest req) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		try {
			resultMap.putAll(jwtService.get(req.getHeader("access-token")));
			resultMap.put("status", true);

			status = HttpStatus.ACCEPTED;
			
		}catch(RuntimeException e){
			System.out.println("????????? ??????");
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
		
	}
	
	@Transactional
	@GetMapping("/tokenUpdate/{userid}")
	@ApiOperation(value = "?????? ???????????? ??? ?????? ??????")
	public ResponseEntity<Map<String, Object>> gettokenValid(@PathVariable int userid) throws Exception{
		User user = userDao.getUserByUserid(userid);
		
	   	String token = "";
    	Map<String, Object> resultMap = new HashMap<>();
		ResponseEntity<Map<String, Object>> entity = null;
    	try {
    		token = jwtService.create(user);
			
			resultMap.putAll(jwtService.get(token));
			
			resultMap.put("status", true);
			resultMap.put("data", user);
			resultMap.put("token", token);
			
			entity = ResponseEntity.accepted().header("access-token", token).body(resultMap);
    	}catch(RuntimeException e){
			//Logger.info("????????? ??????",e);
			resultMap.put("message", e.getMessage());
			entity = ResponseEntity.badRequest().body(resultMap);
		}
    	
		
		return entity;
		
	}
	
	@ApiOperation(value = "name, phone?????? ????????? ??????. 'success' or 'fail'", response = String.class)
	@GetMapping("/search_id/{name}/{phone}")
	public ResponseEntity<String> search_id(@PathVariable("name") String name,
											@PathVariable("phone") String phone) {
		System.out.println("name : "+name+", phone : "+phone);
		Optional<User> userOpt = userDao.findUserByNameAndPhone(name, phone);
		System.out.println("userOpt : "+userOpt);
		if (userOpt.isPresent()) {
            return new ResponseEntity<>(userOpt.get().getEmail(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
        }
	}
	
	@ApiOperation(value = "?????? ??????", response = User.class)
	@GetMapping("/select/{user_id}")
	public ResponseEntity<User> select(@PathVariable int user_id) {
		User user = userDao.getUserByUserid(user_id);
		
		if (user!=null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
	}
	
	@ApiOperation(value = "?????? ?????? ??? ?????????", response = User.class)
	@GetMapping("/rank/{user_id}")
	public ResponseEntity<Map<String, Float>> rank(@PathVariable int user_id) {
		
		Map<String, Float> map = new HashMap<>();
		
		List<User> userList = userDao.findAll();
		
		Collections.sort(userList, (o1, o2)-> {
			return Integer.compare(o2.getExperience(),o1.getExperience());
		});
		
		int count =1;
		for(User u: userList) {
			if(u.getUserid()==user_id) break;
			count++;
		}
		
		map.put("rank", (float)count);
		map.put("percent", ((float)count / userList.size())*100);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@ApiOperation(value = "?????? ????????? ?????? ??????", response = String.class)
    @PostMapping(value = "/modify_pic/{user_id}")
	public ResponseEntity<String> modify_pic(@PathVariable int user_id, @RequestBody List<String> pic) throws IOException {
    	User useropt = userDao.getUserByUserid(user_id);
    	useropt.setFilepath(pic.get(1));
    	userDao.save(useropt);

		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "?????? ?????? ??????", response = String.class)
    @PutMapping(value = "/modify_user")
	public ResponseEntity<String> modify_user(@RequestBody User request) throws IOException {

    	User user = userDao.getUserByUserid(request.getUserid());
    	
    	user.setChallengeId(request.getChallengeId());
    	user.setAccount_open(request.getAccount_open());
    	user.setNickname(request.getNickname());
    	user.setPhone(request.getPhone());
    	if(request.getPassword().length()>0) user.setPassword(passwordEncoder.encode(request.getPassword()));
    	userDao.save(user);

    	return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}

    @ApiOperation(value = "?????? ??????", response = String.class)
	@DeleteMapping("/delete/{user_id}")
	public ResponseEntity<String> delete(@PathVariable int user_id) {
    	User user = userDao.getUserByUserid(user_id);
		if(user!=null) {
			
			// post ?????? ?????? 
			// ?????? ??? ??? ?????? ?????? ????????? ??????
			List<Post> pList = postDao.findByUserId(user);
			for (Post p : pList) {
				Optional<List<PostComment>> pcList = postCommentDao.findByPostIdPostId(p.getPostId());
				if(pcList.isPresent()) {
					for (PostComment pc : pcList.get()) {
						List<PostReply> prList = postReplyDao.findByPostCommentId(pc);
						
						for (PostReply pr : prList) {
							List<PostReplyLike> prlList = postReplyLikeDao.findByPostReplyId(pr);
							for (PostReplyLike prl : prlList) {
								postReplyLikeDao.delete(prl);
							}
							postReplyDao.delete(pr);
						}
						
						List<PostCommentLike> pclList = postCommentLikeDao.findByPostCommentId(pc);
						for (PostCommentLike pcl : pclList) {
							postCommentLikeDao.delete(pcl);
						}
						
						postCommentDao.delete(pc);
					}
				}
				List<PostLike> plList = postLikeDao.findByPostId(p);
				for (PostLike pl : plList) {
					postLikeDao.delete(pl);
				}
				
				List<PostPhoto> photoList = postPhotoDao.findPostPhotoByPostId(p);
				for (PostPhoto pp : photoList) {
					postPhotoDao.delete(pp);
				}
				postDao.delete(p);
			}
			
			// ?????? ?????? ????????? ??? ??? ??????
			List<PostLike> plList = postLikeDao.findByUserId(user);
			for (PostLike pl : plList) {
				postLikeDao.delete(pl);
			}
			
			List<PostReply> prList = postReplyDao.findByUserId(user);
			for (PostReply pr : prList) {
				postReplyController.delete(pr.getPostReplyId());
			}
			
			List<PostComment> pcList = postCommentDao.findByUserId(user);
			for (PostComment pc : pcList) {
				postCommentController.delete(pc.getPostCommentId());
			}
			
			
			//recipe ?????? ??????
			// ??? recipe ?????? ?????? ????????? ??????
			
			List<Recipe> rList = recipeDao.findByUserid(user);
			for (Recipe r : rList) {
				Optional<List<RecipeComment>> rcList = recipeCommentDao.findByRecipeIdRecipeId(r.getRecipeId());
				if(rcList.isPresent()) {
					for (RecipeComment rc : rcList.get()) {
						List<RecipeReply> rrList = recipeReplyDao.findByRecipeCommentId(rc);
						
						for (RecipeReply rr : rrList) {
							List<RecipeReplyLike> rrlList = recipeReplyLikeDao.findByRecipeReplyId(rr);
							for (RecipeReplyLike rrl : rrlList) {
								recipeReplyLikeDao.delete(rrl);
							}
							recipeReplyDao.delete(rr);
						}
						
						List<RecipeCommentLike> rclList = recipeCommentLikeDao.findByRecipeCommentId(rc);
						for (RecipeCommentLike rcl : rclList) {
							recipeCommentLikeDao.delete(rcl);
						}
						
						recipeCommentDao.delete(rc);
					}
				}
				
				List<RecipeLike> rlList = recipeLikeDao.findByRecipeId(r);
				for (RecipeLike rl : rlList) {
					recipeLikeDao.delete(rl);
				}
				
				List<RecipeProcess> rpList = recipeProcessDao.findByRecipeId(r);
				for (RecipeProcess rp : rpList) {
					recipeProcessDao.delete(rp);
				}
				
				recipeDao.delete(r);
			}
			
			// ?????? ?????? ????????? ??? ??? ??????
			List<RecipeReplyLike> rrlList = recipeReplyLikeDao.findByUserId(user);
			for (RecipeReplyLike rrl : rrlList) {
				recipeReplyLikeDao.delete(rrl);
			}
			
			List<RecipeCommentLike> rclList = recipeCommentLikeDao.findByUserId(user);
			for (RecipeCommentLike rcl : rclList) {
				recipeCommentLikeDao.delete(rcl);
			}
			
			List<RecipeLike> rlList = recipeLikeDao.findByUserId(user);
			for (RecipeLike rl : rlList) {
				recipeLikeDao.delete(rl);
			}
			
			List<RecipeReply> rrList = recipeReplyDao.findByUserId(user);
			for (RecipeReply rr : rrList) {
				recipeReplyDao.delete(rr);
			}
			
			List<RecipeComment> rcList = recipeCommentDao.findByUserId(user);
			for (RecipeComment rc : rcList) {
				recipeCommentDao.delete(rc);
			}
			
			// pot follow history ???
			
			List<PotRelation> prlt = potRelationDao.findByUserid(user);
			for(PotRelation p : prlt) {
				potRelationDao.delete(p);
			}
			
			List<Pot> pt = potDao.findByUserid(user);
			for(Pot p : pt) {
				List<PotRelation> ppp = potRelationDao.findByPotid(p);
				for(PotRelation pr : ppp) {
					potRelationDao.delete(pr);
				}
				potDao.delete(p);
			}
			
			Optional<List<Follow>> toflist = followDao.findByToIdUserid(user.getUserid());
			if(toflist.isPresent()) {
				for(Follow f : toflist.get()) {
					followDao.delete(f);
				}
			}
			
			Optional<List<Follow>> frflist = followDao.findByFromIdUserid(user.getUserid());
			if(frflist.isPresent()) {
				for(Follow f : frflist.get()) {
					
					followDao.delete(f);
				}
			}
			
			List<Report> rp = reportDao.findByFromId(user);
			for(Report r : rp) {
				reportDao.delete(r);
			}
			
			rp = reportDao.findByToId(user);
			for(Report r : rp) {
				reportDao.delete(r);
			}
			
			Optional<List<ChallengeHistory>> chl = challengeHistoryDao.findByUserIdUserid(user.getUserid());
			if(chl.isPresent()) {
				for(ChallengeHistory ch : chl.get()) {
					challengeHistoryDao.delete(ch);
				}
			}
			
			List<UserLog> logList = userLogDao.findByUserId(user);
			for (UserLog ul : logList) {
				userLogDao.delete(ul);
			}
			
			List<ChallengeCertification> ccf = challengeCertificationDao.findByUserId(user);
			if(ccf!=null) {
				for(ChallengeCertification c : ccf) {
					challengeCertificationDao.delete(c);
				}
			}
			
			userDao.delete(user);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
    
    @ApiOperation(value = "?????? ?????? ?????????", response = String.class)
    @PutMapping(value = "/lock/{user_id}/{account_open}")
	public ResponseEntity<String> lock(@PathVariable int user_id,@PathVariable int account_open) throws IOException {

    	User user = userDao.getUserByUserid(user_id);
    	
    	user.setAccount_open(account_open);
    	userDao.save(user);

    	return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
}
