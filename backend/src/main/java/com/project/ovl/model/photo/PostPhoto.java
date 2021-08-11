package com.project.ovl.model.photo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostPhoto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_photo_id")
    private int postPhotoId;
	
	private String filepath; // 파일 저장 경로
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postId")
    private Post postId;
}
