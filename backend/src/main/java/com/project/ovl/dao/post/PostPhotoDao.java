package com.project.ovl.dao.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.photo.PostPhoto;

public interface PostPhotoDao extends JpaRepository<PostPhoto, Integer>{
	PostPhoto findPostPhotoByPostPhotoId(int photo_id);
}
