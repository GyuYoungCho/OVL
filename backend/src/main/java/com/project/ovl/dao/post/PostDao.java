package com.project.ovl.dao.post;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ovl.model.post.Post;
import com.project.ovl.model.recipe.Recipe;
import com.project.ovl.model.user.User;

public interface PostDao extends JpaRepository<Post, Integer> {
	Post findPostByPostId(int post_id);
	List<Post> findByUserId(User user);
	int countByUserIdAndCategoryAndTimeBetween(User user, int category, Date start, Date end);
	
	@Query(value = "select * from post p where " +
			"lower(p.content) like :keyword ", nativeQuery = true)
	Page<Post> findByKeyWord(String keyword, Pageable pageable);
}
