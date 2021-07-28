package com.project.ovl.model.pot;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class PotRequest {

	@ApiModelProperty(required = true)
	@NotNull
	String title;
	@ApiModelProperty(required = true)
	@NotNull
	String place;
	@ApiModelProperty(required = true)
	@NotNull
	String step;
	@ApiModelProperty(required = true)
	@NotNull
	String content;
	@ApiModelProperty(required = true)
	@NotNull
	int total_people;
	@ApiModelProperty(required = true)
	@NotNull
	String restaurant_name;
	@ApiModelProperty(required = true)
	@NotNull
	String type;
	@ApiModelProperty(required = true)
	@NotNull
	int userid;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTotal_people() {
		return total_people;
	}
	public void setTotal_people(int total_people) {
		this.total_people = total_people;
	}
	public String getRestaurant_name() {
		return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int user_id) {
		this.userid = user_id;
	}

	
}
