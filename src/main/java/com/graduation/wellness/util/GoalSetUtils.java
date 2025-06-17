package com.graduation.wellness.util;

import com.graduation.wellness.model.enums.Goal;

import java.util.EnumMap;
import java.util.Map;

public class GoalSetUtils {
    private static final Map<Goal, String> GOAL_SET_MAP = new EnumMap<>(Goal.class);
    static {
        GOAL_SET_MAP.put(Goal.WEIGHT_CUT, "Sets: 3 - Reps 12 ~ 15");
        GOAL_SET_MAP.put(Goal.INCREASE_STRENGTH, "Sets: 3 - Reps 3 ~ 6");
        GOAL_SET_MAP.put(Goal.BUILD_MUSCLE, "Sets: 3 - Reps 8 ~ 12");
    }

    public static String getGoalSets(Goal goal) {
        return GOAL_SET_MAP.get(goal);
    }
}

