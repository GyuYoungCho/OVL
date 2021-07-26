
package com.project.ovl.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
	 User getUserByEmail(String email);
	 User getUserByNickname(String nickname);
	 User getUserByUserid(int userid);
	 Optional<User> findUserByNameAndPhone(String name, String phone);
	 Optional<User> findUserByEmailAndPassword(String email, String password);

}
