package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.DayProgressDTO;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.model.entity.UserPlanWeekDayExercise;
import com.graduation.wellness.repository.UserInfoRepository;
import com.graduation.wellness.security.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgressService {

    private final UserInfoRepository userInfoRepo;
    private final JwtTokenUtils jwtTokenUtils;

    public List<DayProgressDTO> getUserProgress() {

        Long userId = getCurrentUserId();
        UserInfo userInfo = userInfoRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserInfo not found"));

        if (userInfo.getUserPlan() == null) {
            throw new EntityNotFoundException("User has no assigned plan");
        }

        List<DayProgressDTO> progressList = new ArrayList<>();

        userInfo.getUserPlan().getWeeks().forEach(week -> week.getDays().forEach(day -> {
            int total = day.getExercises().size();
            int done = (int) day.getExercises().stream().filter(UserPlanWeekDayExercise::isExerciseDone).count();
            progressList.add(new DayProgressDTO(day.getDayNumber(), total, done));
        }));

        return progressList;
    }

    public Long getCurrentUserId() {
        return jwtTokenUtils.getIdFromToken(jwtTokenUtils.getJwtToken());
    }
}
