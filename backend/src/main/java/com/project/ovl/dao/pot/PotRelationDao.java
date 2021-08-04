package com.project.ovl.dao.pot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.pot.PotRelation;
import com.project.ovl.model.user.User;

public interface PotRelationDao extends JpaRepository<PotRelation, Integer>{

	PotRelation getPotRelationByuserid(int userid);
	PotRelation getPotRelationBypotid(int potid);
	List<PotRelation> findByUserid(User user);
}
