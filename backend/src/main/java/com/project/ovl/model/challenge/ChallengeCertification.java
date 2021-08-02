package com.project.ovl.model.challenge;

import java.util.Date;

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
public class ChallengeCertification {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "challenge_certification_id")
	private int challengeCertificationId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challengeId")
    private Challenge challengeId;
	
	private Date certification_date;
	private int certification;
}
