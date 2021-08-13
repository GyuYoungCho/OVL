package com.project.ovl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.ovl.controller.challenge.ChallengeController;
import com.project.ovl.dao.challenge.ChallengeCertificationDao;
import com.project.ovl.dao.challenge.ChallengeDao;
import com.project.ovl.dao.user.UserDao;
import com.project.ovl.model.challenge.Challenge;
import com.project.ovl.model.challenge.ChallengeCertification;
import com.project.ovl.model.user.User;

@Component
public class ChallengeSchedular {

	@Autowired
	ChallengeDao challengeDao;

	@Autowired
	ChallengeCertificationDao challengeCertificationDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	ChallengeController challengeController;

	@Scheduled(cron = "0 11 18 * * *")
	public void challengeJobSchedualing() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String strDate = sdf.format(now);
		List<Challenge> challList = challengeDao.findAll();

		for (Challenge ch : challList) {
			
			// 챌린지 끝날 게 있을 경우

			Calendar endcal = Calendar.getInstance();
			endcal.setTime(ch.getStart_date());
			endcal.add(Calendar.DATE, ch.getPeriod()-1);
			if (sdf.format(endcal.getTime()).equals(strDate)) {
				Optional<List<User>> attendList = userDao.findByChallengeIdChallengeId(ch.getChallengeId());
				
				int all = ch.getPeriod()/ch.getCycle();
				if (attendList.isPresent()) {
					for (User user : attendList.get()) {
						List<ChallengeCertification> ccList = challengeCertificationDao.findByUserId(user);
						
						int count = 0;
						for(ChallengeCertification c : ccList) {
							if(c.getCertification()==1) count++;
						}
						
						float complete_percent = (float)count/all;
						int div =1;
						
						if(complete_percent==1) div=1;
						else if(complete_percent>0.9) div=2;
						else if(complete_percent>0.8) div=4;
						else continue;
						//여기서 complete 처리
						challengeController.complete(user.getUserid(),div);
						
						
						// 인증 기록 db 지우기
						for(ChallengeCertification c : ccList) {
							challengeCertificationDao.delete(c);
						}
					}
				}
			}
						
			// 챌린지 시작할 게 있을 경우
			if (sdf.format(ch.getStart_date()).equals(strDate)) {
				Optional<List<User>> attendList = userDao.findByChallengeIdChallengeId(ch.getChallengeId());

				if (attendList.isPresent()) {

					int dat = ch.getPeriod() / ch.getCycle();

					for (User user : attendList.get()) {
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(ch.getStart_date());

						for (int i = 0; i < dat; i++) {
							challengeCertificationDao.save(new ChallengeCertification(0, user, ch, cal.getTime(), 0));
							cal.add(Calendar.DATE, ch.getCycle());
						}

					}
				}
			}

			
			// 챌린 중
			
		}
	}
}
