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
	
	Page<User> findFromIdByToIdUserid(int user_id, Pageable pageable);
	Page<User> findToIdByFromIdUserid(int user_id, Pageable pageable);

}