
package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
	 User getUserByEmail(String email);
	 User getUserByNickname(String nickname);
	 User login(String email, String password);
}
