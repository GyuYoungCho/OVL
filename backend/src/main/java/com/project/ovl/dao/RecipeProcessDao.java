package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.recipe.RecipeProcess;

public interface RecipeProcessDao extends JpaRepository<RecipeProcess, String>{

}
