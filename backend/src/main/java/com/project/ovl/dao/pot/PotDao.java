package com.project.ovl.dao.pot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.pot.Pot;
import com.project.ovl.model.user.User;

public interface PotDao extends JpaRepository<Pot, String>{

	Pot getPotByPotid(int potid);
	Pot findPotByPotid(int potid);
	List<Pot> findByUserid(User user);
}
