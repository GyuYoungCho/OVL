package com.project.ovl.model.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int report_id;
	
	@Column(columnDefinition = "text")
	private String reason;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fromId")
    private User fromId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "toId")
    private User toId;
}
