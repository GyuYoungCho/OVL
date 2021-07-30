package com.project.ovl.model.recipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.RecipeDao;
import com.project.ovl.dao.RecipeProcessDao;

@Component
public class RecipePhotoHandler {

	@Autowired
	RecipeDao recipeDao;
	
	@Autowired
	RecipeProcessDao processDao;
	
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

	public List<RecipeProcess> getProcessList(List<MultipartFile> multipartFiles, List<String> contentList, int recipeId) throws Exception {
		// 반환할 파일 리스트
		List<RecipeProcess> processList = new ArrayList<>();

		// 전달되어 온 파일이 존재할 경우
		if (!CollectionUtils.isEmpty(multipartFiles)) {
			// 파일명을 업로드 한 날짜로 변환하여 저장
//			LocalDateTime now = LocalDateTime.now();
//			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//			String current_date = now.format(dateTimeFormatter);

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
			// 파일을 저장할 세부 경로 지정
			String path = "src/main/resources/static/recipe/" + recipeId;
			File file = new File(path);
			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful)
					System.out.println("file: was not successful");
			}

			// 다중 파일 처리
			for (int i=0;i<multipartFiles.size();i++) {
				// 파일의 확장자 추출
				String originalFileExtension;
				String contentType = multipartFiles.get(i).getContentType();
				
				// 확장자명이 존재하지 않을 경우 처리 x
				if (ObjectUtils.isEmpty(contentType)) {
					break;
				} else { // 확장자가 jpeg, png인 파일들만 받아서 처리
					if (contentType.contains("image/jpeg"))
						originalFileExtension = ".jpg";
					else if (contentType.contains("image/png"))
						originalFileExtension = ".png";
					else // 다른 확장자일 경우 처리 x
						break;
				}
				
				// 파일명 중복 피하고자 나노초까지 얻어와 지정
				String new_file_name = System.nanoTime() + originalFileExtension;
				
				Recipe recipe = recipeDao.findRecipeByRecipeId(recipeId);
				
				RecipeProcess process = new RecipeProcess(0, contentList.get(i), multipartFiles.get(i).getOriginalFilename(), path + "/" + new_file_name, multipartFiles.get(i).getSize()+"", recipe);
						
				processList.add(process);
				
				// 업로드 한 파일 데이터를 지정한 파일에 저장
				file = new File(absolutePath + path + File.separator + new_file_name);
				multipartFiles.get(i).transferTo(file);

				// 파일 권한 설정(쓰기, 읽기)
				file.setWritable(true);
				file.setReadable(true);
			}
			
		}
		return processList;
	}
}
