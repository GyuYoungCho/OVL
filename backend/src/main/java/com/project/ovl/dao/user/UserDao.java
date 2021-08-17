
package com.project.ovl.dao.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
	 User getUserByEmail(String email);
	 User getUserByNickname(String nickname);
	 User getUserByUserid(int userid);
	 Optional<User> findUserByNameAndPhone(String name, String phone);
	 Optional<User> findUserByEmailAndPassword(String email, String password);
	 
	 Optional<List<User>> findByChallengeIdChallengeId(int challenge_id);
	 
	 @Query(value = "select * from user u where " +
				"lower(u.nickname) like :keyword", nativeQuery = true)
	 Page<User> findByKeyWord(String keyword, Pageable pageable);
}
