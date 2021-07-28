package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.pot.PotRelation;

public interface PotRelationDao extends JpaRepository<PotRelation, Integer>{

	PotRelation getPotRelationByuserid(int userid);
}
