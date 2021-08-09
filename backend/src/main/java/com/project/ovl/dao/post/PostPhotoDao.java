package com.project.ovl.dao.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;

public interface PostPhotoDao extends JpaRepository<PostPhoto, Integer>{
	PostPhoto findPostPhotoByPostPhotoId(int photo_id);
	List<PostPhoto> findPostPhotoByPostId(Post p);
}
