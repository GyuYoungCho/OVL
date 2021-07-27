package com.project.ovl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.photo.PostPhoto;

public interface PostPhotoDao extends JpaRepository<PostPhoto, Integer>{

}
