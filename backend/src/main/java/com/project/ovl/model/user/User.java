package com.project.ovl.model.user;

<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;

>>>>>>> 590773aefb51e345ba14c9dab9ce93113200fc5a
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<<<<<<< HEAD
=======
import javax.persistence.Lob;
import javax.persistence.OneToMany;
>>>>>>> 590773aefb51e345ba14c9dab9ce93113200fc5a

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
<<<<<<< HEAD
=======
import com.project.ovl.model.post.Post;
>>>>>>> 590773aefb51e345ba14c9dab9ce93113200fc5a

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userid;

    
    private String email;
    private String nickname;
    private String name;
    private String phone;
    private String password;
    private int experience;
    private int account_open;
    private int warning;
    
<<<<<<< HEAD
    private String original_file_name;
    private String stored_file_path;
=======
    
    private String original_file_name;
    private String stored_file_path;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();
    
    
>>>>>>> 590773aefb51e345ba14c9dab9ce93113200fc5a
}