
package com.project.ovl.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
	 User getUserByEmail(String email);
	 User getUserByNickname(String nickname);
	 User login(String email, String password);
	 User getUserByUserid(int userid);
	 Optional<User> findUserByNameAndPhone(String name, String phone);
}
