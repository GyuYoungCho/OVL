package com.project.ovl.model.pot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ovl.model.post.Post;
import com.project.ovl.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pot {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pot_id;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}


/**CREATE TABLE IF NOT EXISTS `mydb`.`pot` (
  `pot_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `place` VARCHAR(100) NOT NULL,
  `time` DATETIME NOT NULL,
  `step` VARCHAR(20) NOT NULL,
  `content` VARCHAR(100) NOT NULL,
  `total_people` INT NOT NULL,
  `restaurant_name` VARCHAR(100) NOT NULL,
  `type` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`pot_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`pot_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`pot_relation` (
  `pot_relation_id` INT NOT NULL AUTO_INCREMENT,
  `pot_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`pot_relation_id`),
  INDEX `fk_pot_relation_pot1_idx` (`pot_id` ASC),
  INDEX `fk_pot_relation_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_pot_relation_pot1`
    FOREIGN KEY (`pot_id`)
    REFERENCES `mydb`.`pot` (`pot_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pot_relation_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



 * 
 */
