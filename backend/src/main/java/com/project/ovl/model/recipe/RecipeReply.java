package com.project.ovl.model.recipe;

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
public class RecipeReply {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_reply_id")
    private int recipeReplyId;
	
	private String content;
	private int like_count;
	private Date time;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipeCommentId")
    private RecipeComment recipeCommentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
}
