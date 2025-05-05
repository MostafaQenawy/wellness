package com.graduation.wellness.util;

import lombok.experimental.UtilityClass;

import java.util.NavigableMap;
import java.util.TreeMap;

@UtilityClass
public class BMIClassifier {
    private static final NavigableMap<Double, String> bmiCategories = new TreeMap<>();

    static {
        bmiCategories.put(18.5, "Underweight");
        bmiCategories.put(24.9, "Normal weight");
        bmiCategories.put(30.0, "Overweight");
        bmiCategories.put(Double.MAX_VALUE, "Obesity");
    }

    public static String classifyBMI(double bmi) {
        return bmiCategories.ceilingEntry(bmi).getValue();
    }
}
