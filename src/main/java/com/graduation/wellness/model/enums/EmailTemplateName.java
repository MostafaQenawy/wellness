package com.graduation.wellness.model.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("Activate Account"),
    CHANGE_PASSWORD("Change Your Password");

    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}