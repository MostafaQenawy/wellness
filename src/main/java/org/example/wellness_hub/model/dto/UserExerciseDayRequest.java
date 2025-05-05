package org.example.wellness_hub.model.dto;

public record UserExerciseDayRequest(long userID, long exerciseID, long dayID, long weekID) {
}
