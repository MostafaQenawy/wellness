package com.graduation.wellness.util;

import lombok.experimental.UtilityClass;

import java.util.NavigableMap;
import java.util.TreeMap;

@UtilityClass
public class AgeClassifier {

    private static final NavigableMap<Integer, String> ageGroups = new TreeMap<>();

    static {
        ageGroups.put(12, "Child");
        ageGroups.put(18, "Youth");
        ageGroups.put(35, "Adults");
        ageGroups.put(55, "Middle Aged");
        ageGroups.put(Integer.MAX_VALUE, "Elderly");
    }

    public static String classifyAge(int age) {
        return ageGroups.ceilingEntry(age).getValue();
    }
}
