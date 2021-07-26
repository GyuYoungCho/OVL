package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.follow.Follow;


public interface FollowDao extends JpaRepository<Follow, String>{

	List<Follow> findByFromuserUserid(int fromuser);
	List<Follow> findByTouserUserid(int touser);
}