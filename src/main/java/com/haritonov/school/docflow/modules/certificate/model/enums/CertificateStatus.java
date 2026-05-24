package com.haritonov.school.docflow.modules.certificate.model.enums;

public enum CertificateStatus {
    NOT_STARTED("Не началась", "secondary"),
    ACTIVE("Действует", "success"),
    EXPIRING_SOON("Истекает через 3 дня", "warning"),
    EXPIRED("Истекла", "danger");

    private final String description;
    private final String badgeClass;

    CertificateStatus(String description, String badgeClass) {
        this.description = description;
        this.badgeClass = badgeClass;
    }

    public String getDescription() {
        return description;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
