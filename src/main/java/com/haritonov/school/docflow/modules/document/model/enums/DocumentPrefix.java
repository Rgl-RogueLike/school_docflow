package com.haritonov.school.docflow.modules.document.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentPrefix {

    ENROLLMENT("Пр-Зач", "Приказ о зачислении (одиночный)"),
    ENROLLMENT_MORE("Пр-Зач-Груп", "Приказ о зачислении (групповой)"),
    DISLOCATION("Пр-Пер", "Приказ о временном перемещении"),
    RELEASE("Пр-Отч", "Приказ об отчислении"),
    CERTIFICATE("Спр-Осв", "Справка об освобождении"),
    EXEMPTION_ORDER("Пр-Осв", "Приказ об освобождении");  // ← НОВЫЙ ПРЕФИКС

    private final String code;
    private final String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}