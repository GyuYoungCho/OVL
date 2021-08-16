package com.project.ovl.dao.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ovl.model.photo.PostPhoto;
import com.project.ovl.model.post.Post;

public interface PostPhotoDao extends JpaRepository<PostPhoto, Integer>{
	PostPhoto findPostPhotoByPostPhotoId(int photo_id);
	List<PostPhoto> findPostPhotoByPostId(Post p);
	
	@Query(value="select * " +
			"from post_photo ph join post p on p.post_id = ph.post_id " + 
			"where p.post_id in " +
			"(select post_id from post where user_id in " + 
			"(select to_id from follow where from_id = :user_id )" + 
			"and TIMESTAMPDIFF(Day, time, now()) < 7) " +
			"group by p.post_id " + 
			"order by time desc"
			, nativeQuery = true)
	Page<PostPhoto> findByUserFollowing(int user_id,Pageable pageable);
	
	@Query(value="select min(post_photo_id),filepath, time, p.post_id " +  
			"from post_photo ph join post p on p.post_id = ph.post_id " + 
			"where TIMESTAMPDIFF(Day, time, now()) < 7) " +
			"group by p.post_id " +
			"order by time desc like_count desc comment_count desc "
			, nativeQuery = true)
	Page<PostPhoto> findByComing(Pageable pageable);
	
	
	// 검색 고고
	@Query(value="select min(post_photo_id),filepath, time, p.post_id " +  
			"from post_photo ph join post p on p.post_id = ph.post_id " + 
			"where TIMESTAMPDIFF(Day, time, now()) < 7) " +
			"group by p.post_id " +
			"order by time desc like_count desc comment_count desc ", nativeQuery = true)
	Page<PostPhoto> findByContentkeyword(String keyword, Pageable pageable);
}
