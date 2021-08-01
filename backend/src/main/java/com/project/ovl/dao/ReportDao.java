package com.project.ovl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.report.Report;
import com.project.ovl.model.user.User;

public interface ReportDao extends JpaRepository<Report, String> {
	Report findReportByFromIdAndToId(User fromUser, User toUser);
	List<Report> findByFromId(User user);
	List<Report> findByToId(User user);
}
