package com.project.ovl.model.recipe;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.recipe.RecipeDao;
import com.project.ovl.dao.recipe.RecipeProcessDao;

@Component
public class RecipePhotoHandler {

	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	RecipeProcessDao processDao;
	
	private final String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
	
	public File dirSetting(String path) {
		// 파일을 저장할 세부 경로 지정
		File file = new File(path);
		if (!file.exists()) {
			boolean wasSuccessful = file.mkdirs();

			// 디렉터리 생성에 실패했을 경우
			if (!wasSuccessful)
				System.out.println("file: was not successful");
		}
		
		return file;
	}
	
	public String pathSetting(MultipartFile photo) {
		String originalFileExtension = "";
		String contentType = photo.getContentType();
		
		// 확장자명이 존재하지 않을 경우 처리 x
		if (ObjectUtils.isEmpty(contentType)) {
		} else { // 확장자가 jpeg, png인 파일들만 받아서 처리
			if (contentType.contains("image/jpeg"))
				originalFileExtension = ".jpg";
			else if (contentType.contains("image/png"))
				originalFileExtension = ".png";
			else return null;
		}
		String new_file_name = System.nanoTime() + originalFileExtension;
		
		return new_file_name;
	}
	
	public void photoSave(File file, String new_file_name, String path, MultipartFile photo) throws IllegalStateException, IOException {
		// 업로드 한 파일 데이터를 지정한 파일에 저장
		file = new File(absolutePath + path + File.separator + new_file_name);
		photo.transferTo(file);

		// 파일 권한 설정(쓰기, 읽기)
		file.setWritable(true);
		file.setReadable(true);
	}
	
	public void saveProfile(List<MultipartFile> pic, int recipeId) throws IllegalStateException, IOException {
		if (CollectionUtils.isEmpty(pic)) {
			String path = "src/main/resources/static/recipe/" + recipeId;
			File file = dirSetting(path);
			
			for (int i=1;i<pic.size();i++) {
				String new_file_name = pathSetting(pic.get(i));
				
				Recipe recipe = recipeDao.findRecipeByRecipeId(recipeId);
				recipe.setOriginal_file_name(pic.get(i).getOriginalFilename());
				recipe.setStored_file_path(path + "/" + new_file_name);
				recipeDao.save(recipe);
				
				photoSave(file, new_file_name, path, pic.get(i));
			}
		}
	}

	public void saveProcess(List<MultipartFile> photoList, List<String> contentList, List<Integer> idList, int recipeId, int type) throws Exception { // type 0 : 등록, 1 : 수정
		if (!CollectionUtils.isEmpty(photoList)) {
			String path = "src/main/resources/static/recipe/" + recipeId;
			File file = dirSetting(path);
			
			// 다중 파일 처리
			for (int i=1;i<photoList.size();i++) {
				String new_file_name = pathSetting(photoList.get(i));
				
				Recipe recipe = recipeDao.findRecipeByRecipeId(recipeId);
				
				if (type==0) { // 등록 일 때 
					RecipeProcess process = new RecipeProcess(0, contentList.get(i), photoList.get(i).getOriginalFilename(), path + "/" + new_file_name, photoList.get(i).getSize()+"", recipe);
					processDao.save(process);
				} else { // 사진 수정 일 때
					RecipeProcess process = processDao.findRecipeProcessByRecipeProcessId(idList.get(i));
					process.setFilename(photoList.get(i).getOriginalFilename());
					process.setFilepath(path + "/" + new_file_name);
					process.setFilesize(photoList.get(i).getSize()+"");
					processDao.save(process);
				}
					
				photoSave(file, new_file_name, path, photoList.get(i));
			}
			
		}
	}
}
