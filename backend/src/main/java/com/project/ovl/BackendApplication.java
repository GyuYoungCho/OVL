package com.project.ovl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.ovl.interceptor.JwtInterceptor;


@SpringBootApplication
@EnableScheduling
public class BackendApplication implements WebMvcConfigurer{

	
	/// test
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
	@Autowired
	private JwtInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(jwtInterceptor).addPathPatterns("/user/**")
	    .excludePathPatterns(Arrays.asList("/user/join", "/user/login","/user/nickname_check/{nickname}", "/user/search_id/{name}/{phone}", 
	    		"/user/select/{user_id}", "/user/email_auth/{type}/{email}", "/user/email_auth_check/{email}/{confirm}",
	    		"/user/delete/{user_id}","/user/modify_user/{user_id}","/user/rank/{user_id}"));
	    
	}


	/**
	 * 전역 cross origin
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	    .allowedOrigins("*")
	    .allowedMethods("*")
	    .allowedHeaders("*")
	    .exposedHeaders("access-token");
	}
	
	

}
