package com.project.ovl.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.pot.Pot;

public interface PotDao extends JpaRepository<Pot, String>{

	Pot getPotByPotid(int potid);
}
