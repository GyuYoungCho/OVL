package com.project.ovl.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.follow.Follow;


public interface FollowDao extends JpaRepository<Follow, Integer>{

	Optional<List<Follow>> findByFromIdUserid(int fromIdUserid);
	Optional<List<Follow>> findByToIdUserid(int toIdUserid);
	@Transactional
	int deleteByfromIdUseridAndToIdUserid(int fromIdUserid, int toIdUserid);
}