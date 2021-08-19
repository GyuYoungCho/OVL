package com.project.ovl.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.user.User;
import com.project.ovl.model.user.UserLog;

public interface UserLogDao extends JpaRepository<UserLog, String> {
	List<UserLog> findTop300ByUserIdOrderByLogDateDesc(User user);
	UserLog findByTypeAndContentId(int type, int content_id);
	List<UserLog> findByUserId(User user);
}
