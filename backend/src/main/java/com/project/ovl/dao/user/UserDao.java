
package com.project.ovl.dao.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
	 User getUserByEmail(String email);
	 User getUserByNickname(String nickname);
	 User getUserByUserid(int userid);
	 Optional<User> findUserByNameAndPhone(String name, String phone);
	 Optional<User> findUserByEmailAndPassword(String email, String password);
	 
	 Optional<List<User>> findByChallengeIdChallengeId(int challenge_id);
}
