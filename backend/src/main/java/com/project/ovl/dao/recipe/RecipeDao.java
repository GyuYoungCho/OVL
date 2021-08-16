package com.project.ovl.dao.recipe;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.ovl.model.post.Post;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

public interface RecipeDao extends JpaRepository<Recipe, String>{
	Recipe findRecipeByRecipeId(int recipeId);
	List<Recipe> findByTitleContaining(String title);
	List<Recipe> findByUserid(User user);
	int countByUseridAndTimeBetween(User user, Date start, Date end);
	
	
	Page<Recipe> findAll(Pageable pageable);
}
