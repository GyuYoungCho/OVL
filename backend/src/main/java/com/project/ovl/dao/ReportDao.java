package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.report.Report;
import com.project.ovl.model.user.User;

public interface ReportDao extends JpaRepository<Report, String> {
	Report findReportByFromIdAndToId(User fromUser, User toUser);
}
