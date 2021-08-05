package com.project.ovl.model.pot;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurant {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
    private int restaurantid;
	
	private String restaurantName;
	private String kind;
	private String call;
	
	private String gu;
	private String place;
	private String detail;
}
