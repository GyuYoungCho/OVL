package com.project.ovl.model.mail;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class mailService {
	private final JavaMailSenderImpl javaMailSenderImpl;
	
	@Async
	public int mailSend(String email, String type) {
		try {
			MailHandler mailHandler = new MailHandler(javaMailSenderImpl);
			Random random = new Random(System.currentTimeMillis());
			
			int result = 100000 + random.nextInt(9000000);
			mailHandler.setTo(email);
			mailHandler.setFrom("1029huiyoeng@gmail.com");
			if (type.equals("join")) mailHandler.setSubject("Ovl 회원가입 인증번호");
			else if (type.equals("password")) mailHandler.setSubject("Ovl 비밀번호 인증번호");
			StringBuffer htmlContent = new StringBuffer();
			htmlContent.append("<!DOCTYPE html>");
			htmlContent.append("<html>");
			htmlContent.append("<head>");
			htmlContent.append("</head>");
			htmlContent.append("<body>");
			if (type.equals("join")) {
				htmlContent.append(
						" <div" 																																																	+ 
						"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; border-top: 4px solid #02b875; border-bottom: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
						"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
						"		<span style=\"color: #02b875\">Ovl 회원가입 인증번호</span> 안내입니다."																																				+ 
						"	</h1>\n"																																																+ 
						"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
						email																																																+
						"		님 안녕하세요.<br />"																																													+ 
						"		Ovl에 오신걸 진심으로 환영합니다.<br />"																																						+ 
						"		아래 인증번호를 확인하고 회원가입을 완료해주세요.<br />"																													+ 
						"		감사합니다."																																															+ 
						"	</p>"	+
						"	<h2><span style=\"color: #02b875\">인증번호</span>  "
						+"<strong>"+result+"</strong>"+
						"	</h2>"+																																	
						" </div>"	
				);
			}
			else if (type.equals("password")) {
				htmlContent.append(
						" <div" 																																																	+ 
						"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; border-top: 4px solid #02b875; border-bottom: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
						"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
						"		<span style=\"color: #02b875\">Ovl 비밀번호 인증번호</span> 안내입니다."																																				+ 
						"	</h1>\n"																																																+ 
						"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
						email																																																+
						"		님 안녕하세요.<br />"																																									+ 
						"		아래 인증번호를 확인하고 비밀번호 변경을 완료해주세요.<br />"																													+ 
						"		감사합니다."																																															+ 
						"	</p>"	+
						"	<h2><span style=\"color: #02b875\">인증번호</span>  "
						+"<strong>"+result+"</strong>"+
						"	</h2>"+																																	
						" </div>"	
				);
			}
			htmlContent.append("</body>");
			htmlContent.append("</html>");
			mailHandler.setText(htmlContent.toString(), true);
			mailHandler.send();
			
			return result;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
