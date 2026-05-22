package com.haritonov.school.docflow.modules.document.service;

import com.haritonov.school.docflow.model.base.DocumentCounter;
import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.repository.DocumentCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DocumentNumberGeneratedServiceImpl implements DocumentNumberGeneratedService {

    private final DocumentCounterRepository counterRepository;

    @Override
    @Transactional
    public String generatedNumber(DocumentPrefix prefix, LocalDate documentDate) {
        int year = documentDate.getYear();
        int shortYear = year % 100;
        DocumentCounter counter = counterRepository.findByPrefixAndYear(prefix.getCode(), year)
                .orElse(new DocumentCounter(prefix.getCode(), year, 0L));
        long nextNumber = counter.getCurrentNumber() + 1;
        counter.setCurrentNumber(nextNumber);
        counterRepository.save(counter);
        return String.format("%s-%d-%d", prefix.getCode(), nextNumber, shortYear);
    }

    @Override
    @Transactional
    public String generateNumberWithFullYear(DocumentPrefix prefix, LocalDate documentDate) {
        int year = documentDate.getYear();

        DocumentCounter counter = counterRepository.findByPrefixAndYear(prefix.getCode(), year)
                .orElse(new DocumentCounter(prefix.getCode(), year, 0L));

        long nextNumber = counter.getCurrentNumber() + 1;
        counter.setCurrentNumber(nextNumber);
        counterRepository.save(counter);

        return String.format("%s-%d-%d", prefix.getCode(), nextNumber, year);
    }
}
