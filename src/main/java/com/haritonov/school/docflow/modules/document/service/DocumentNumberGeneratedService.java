package com.haritonov.school.docflow.modules.document.service;

import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;

import java.time.LocalDate;

public interface DocumentNumberGeneratedService {

    String generatedNumber(DocumentPrefix prefix, LocalDate documentDate);

    String generateNumberWithFullYear(DocumentPrefix prefix, LocalDate documentDate);
}
