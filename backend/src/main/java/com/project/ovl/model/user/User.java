package com.project.ovl.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.challenge.Challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userid;
  
    private String email;
    private String nickname;
    private String name;
    private String phone;
    
    @JsonIgnore
    private String password;
    private int experience;
    private int account_open;
    private int warning;

    private String filepath;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challengeId")
    private Challenge challengeId;
}