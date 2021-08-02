package com.project.ovl.dao.challenge;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.challenge.ChallengeHistory;

public interface ChallengeHistoryDao extends JpaRepository<ChallengeHistory,String> {
	Optional<List<ChallengeHistory>> findByUserIdUserid(int user_id);
	ChallengeHistory findByUserIdUseridAndChallengeIdChallengeId(int user_id,int challenge_id);
}
