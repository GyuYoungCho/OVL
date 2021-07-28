package com.project.ovl.model.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostComment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_comment_id")
    private int postcommentid;

	private int Type;
    private String content;
    
    private String date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PostId")
    private Post PostId;
}
