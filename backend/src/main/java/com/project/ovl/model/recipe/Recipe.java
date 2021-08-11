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
public class Recipe {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_id")
    private int recipeId;
	
	private String title;
	private String content;
	
	@Column(columnDefinition = "text")
    private String ingredient;
	
	private Date time;
	private int like_count;
	private int comment_count;
	
	private String filepath;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userid;
}
