package com.project.ovl.model.photo;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.ovl.dao.post.PostDao;
import com.project.ovl.dao.post.PostPhotoDao;
import com.project.ovl.model.post.Post;

@Component
public class PhotoHandler {
	@Autowired
	PostDao postDao;
	
	@Autowired
	PostPhotoDao postPhotoDao;
	public void parseFileInfo(List<MultipartFile> photo, int post_id, int type, List<Integer> idList) throws Exception { // type 0 : 등록, 1 : 수정

		// 전달되어 온 파일이 존재할 경우
		if (!CollectionUtils.isEmpty(photo)) {

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
			// 파일을 저장할 세부 경로 지정
			String path = "src/main/resources/static/post/" + post_id;
			File file = new File(path);
			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful)
					System.out.println("file: was not successful");
			}

			// 다중 파일 처리
			for (int i=1;i<photo.size();i++) {

				// 파일의 확장자 추출
				String originalFileExtension;
				String contentType = photo.get(i).getContentType();

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

				Post post = postDao.findPostByPostId(post_id); 
					
				// 파일 DTO 이용하여 Photo 엔티티 생성
				if (type==0) { // 등록
					PostPhoto newPhoto = new PostPhoto(0, photo.get(i).getOriginalFilename(), path + "/" + new_file_name, photo.get(i).getSize()+"", post);
					postPhotoDao.save(newPhoto);
				} else { // 수정
					PostPhoto modifyPhoto = postPhotoDao.findPostPhotoByPostPhotoId(idList.get(i));
					modifyPhoto.setFilename(photo.get(i).getOriginalFilename());
					modifyPhoto.setFilepath(path + "/" + new_file_name);
					modifyPhoto.setFilesize(photo.get(i).getSize()+"");
					postPhotoDao.save(modifyPhoto);
				}
			

				// 업로드 한 파일 데이터를 지정한 파일에 저장
				file = new File(absolutePath + path + File.separator + new_file_name);
				photo.get(i).transferTo(file);

				// 파일 권한 설정(쓰기, 읽기)
				file.setWritable(true);
				file.setReadable(true);
			}
		}
	}
}
