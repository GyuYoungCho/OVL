package com.project.ovl.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.follow.Follow;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.report.Report;

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
    private String password;
    private int experience;
    private int account_open;
    private int warning;
    
    @Lob
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="profile", columnDefinition="longblob", nullable=true)
    private byte[] img;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    
}