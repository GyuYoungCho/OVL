package com.project.ovl.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.post.Post;

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

    @JsonIgnore
    private String email;
    private String nickname;
    private String name;
    private String phone;
    private String password;
    private int experience;
    private int account_open;
    private int warning;
    
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "from_id")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "to_id")
    private List<Follow> followers = new ArrayList<>();
}