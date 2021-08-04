package com.project.ovl.dao.challenge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.user.User;

public interface ChallengeCertificationDao extends JpaRepository<ChallengeCertification, String> {
	List<ChallengeCertification> findByUserId(User user);
}
