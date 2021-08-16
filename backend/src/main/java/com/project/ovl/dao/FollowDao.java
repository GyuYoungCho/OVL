package com.project.ovl.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.user.User;


public interface FollowDao extends JpaRepository<Follow, Integer>{

	Optional<List<Follow>> findByFromIdUserid(int fromIdUserid);
	Optional<List<Follow>> findByToIdUserid(int toIdUserid);
	@Transactional
	int deleteByfromIdUseridAndToIdUserid(int fromIdUserid, int toIdUserid);
//	
//	@Query("select follow  from follow where from_id=:user_id")
//	Page<Follow> findByfollwing(int user_id, Pageable pageable);
//	
//	@Query("select follow  from follow where to_id=:user_id")
//	Page<Follow> findByfollwer(int user_id, Pageable pageable);
}