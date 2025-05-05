package com.graduation.wellness.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BMICalculator {
    public static double BMICalculate(double weight, double height) {
        return weight / (height*height);
    }
}
