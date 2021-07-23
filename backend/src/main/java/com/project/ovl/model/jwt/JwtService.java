package com.project.ovl.model.jwt;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.ovl.controller.UserController;
import com.project.ovl.model.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
	    @Value("${jwt.salt}")
	    private String salt;
	
	    @Value("${jwt.expmin}")
	    private Long expireMin;
	
	    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	    /**
	
	 * 로그인 성공시 사용자 정보를 기반으로 JWTToken을 생성해서 반환한다.
	 */
	
	public String create(final User userDto) {
	    System.out.println("토큰 생성 테스트");
	    logger.info("time: {}", expireMin);
	    final JwtBuilder builder = Jwts.builder();
	    //JWT Token = Header + Payload + Signagure
	    //header 설정
	    builder.setHeaderParam("typ", "JWT");
	    
	    //payload 설정
	    builder.setSubject("로그인 토큰")
	    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin))
	    .claim("UserDto", userDto);
	    
	    //signature
	    builder.signWith(SignatureAlgorithm.HS256, salt.getBytes());
	    
	    final String jwt = builder.compact();
	    logger.info("토큰 발행: {}", jwt);
	    return jwt;
	    
	    
	}
	
	
	/**
	 * 전달 받은 토큰이 제대로 생성 된 것인지 확인하고 문제가 있다면 Runtime 예외를 발생 시킨다. 
	 * 
	 */
	public void checkValid(final String jwt) {
	    logger.info("토큰 점검: {}", jwt);
	    Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
	}
	
	/*
	 * jwt 토큰을 분석해서 필요한 정보를 반환한다.
	 */
	
	public Map<String, Object> get(final String jwt){
	    Jws<Claims> claims = null;
	    
	    try {
	        claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
	    }catch(final Exception e) {
	        throw new RuntimeException();
	    }
	    logger.info("claims: {}", claims);
	    return claims.getBody();
	    
	}	
}
