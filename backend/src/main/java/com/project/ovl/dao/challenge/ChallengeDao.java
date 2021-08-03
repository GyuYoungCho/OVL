package com.project.ovl.dao.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.challenge.Challenge;


public interface ChallengeDao extends JpaRepository<Challenge,Integer> {
	Challenge findByChallengeId(int challenge_id);

}
