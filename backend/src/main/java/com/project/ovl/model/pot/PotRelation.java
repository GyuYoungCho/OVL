package com.project.ovl.model.pot;

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
public class PotRelation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pot_relation_id")
    private int potrelationid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pot_id")
    private Pot potid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userid;
}
