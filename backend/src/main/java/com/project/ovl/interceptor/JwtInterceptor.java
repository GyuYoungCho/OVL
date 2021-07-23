package com.project.ovl.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.ovl.model.jwt.JwtService;


@Component
public class JwtInterceptor implements HandlerInterceptor{
	@Autowired
	private JwtService jwtService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
	    System.out.println(request.getMethod() + " : " + request.getServletPath());
	    
	    if(request.getMethod().equals("OPTIONS")) {
	        return true;
	    }else {
	        //request parameter 에서 access-token으로 넘어온 녀석을 찾아본다.
	        String token = request.getHeader("access-token");
	        System.out.println(token +" :token");
	        if(token != null && token.length() > 0) {
	            jwtService.checkValid(token);
	            System.out.print("토큰 사용 가능");
	            return true;
	        }else {
	            throw new RuntimeException("인증 토큰이 없습니다.");
	        }
	    }
	}
}
