package com.haritonov.school.docflow.modules.student.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("Мужской"),
    FEMALE("Женский");

    private final String displayName;
}
