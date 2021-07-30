package com.project.ovl.model.recipe;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeProccessDao;

public class RecipePhotoHandler {

	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	RecipeProccessDao proccessDao;
	
	public void saveProfile(MultipartFile pic, int recipe_id) throws IllegalStateException, IOException {
		if (pic!=null) {
			String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
			// 파일을 저장할 세부 경로 지정
			String path = "src/main/resources/static/recipe/" + recipe_id;
			File file = new File(path);
			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful)
					System.out.println("file: was not successful");
			}
			
			String originalFileExtension;
			String contentType = pic.getContentType();
			
			if (ObjectUtils.isEmpty(contentType)) {
				return;
			} else { // 확장자가 jpeg, png인 파일들만 받아서 처리
				if (contentType.contains("image/jpeg"))
					originalFileExtension = ".jpg";
				else if (contentType.contains("image/png"))
					originalFileExtension = ".png";
				else // 다른 확장자일 경우 처리 x
					return;
			}
			
			String new_file_name = System.nanoTime() + originalFileExtension;
			
			
			Recipe recipe = recipeDao.findRecipeByRecipeId(recipe_id);
			recipe.setOriginal_file_name(pic.getOriginalFilename());
			recipe.setStored_file_path(path + "/" + new_file_name);
			recipeDao.save(recipe);
			
			// 업로드 한 파일 데이터를 지정한 파일에 저장
			file = new File(absolutePath + path + File.separator + new_file_name);
			pic.transferTo(file);

			// 파일 권한 설정(쓰기, 읽기)
			file.setWritable(true);
			file.setReadable(true);
		}
	}
}
