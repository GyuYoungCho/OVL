package com.project.ovl.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.follow.Follow;


public interface FollowDao extends JpaRepository<Follow, String>{

	List<Follow> findFollowByfromuserUserid(int fromuserUserid);
	List<Follow> findFollowByTouserUserid(int touserUserid);
	@Transactional
	int deleteByfromuserUseridAndTouserUserid(int fromuserUserid, int touserUserid);
}