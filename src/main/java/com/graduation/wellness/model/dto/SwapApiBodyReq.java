package com.graduation.wellness.model.dto;

public record SwapApiBodyReq(Long oldExerciseID, Long newExerciseID, Long dayID, Long weekID) {
}
