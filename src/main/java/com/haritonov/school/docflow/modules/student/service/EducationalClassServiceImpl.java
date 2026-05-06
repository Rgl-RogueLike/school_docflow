package com.haritonov.school.docflow.modules.student.service;

import com.haritonov.school.docflow.modules.student.dto.EducationalClassCreateRequest;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassResponse;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassUpdateRequest;
import com.haritonov.school.docflow.modules.student.model.EducationalClass;
import com.haritonov.school.docflow.modules.student.repository.EducationalClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationalClassServiceImpl implements EducationalClassSerivce {

    private final EducationalClassRepository educationalClassRepository;

    private EducationalClassResponse mapToResponse(EducationalClass entity) {
        EducationalClassResponse response = new EducationalClassResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setYear(entity.getYear());
        return response;
    }

    @Override
    @Transactional
    public Long create(EducationalClassCreateRequest request) {
        EducationalClass educationalClass = new EducationalClass();
        educationalClass.setName(educationalClass.getName());
        educationalClass.setYear(educationalClass.getYear());
        EducationalClass savedClass = educationalClassRepository.save(educationalClass);
        return savedClass.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationalClassResponse> getAll() {
        List<EducationalClass> classes = educationalClassRepository.findAll();
        return classes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EducationalClassResponse getById(Long id) {
        EducationalClass educationalClass = educationalClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));
        return mapToResponse(educationalClass);
    }

    @Override
    @Transactional
    public void update(EducationalClassUpdateRequest request) {
        EducationalClass educationalClass = educationalClassRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));

        educationalClass.setName(request.getName());
        educationalClass.setYear(request.getYear());
        educationalClassRepository.save(educationalClass);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!educationalClassRepository.existsById(id)) {
            throw new IllegalArgumentException("Класс не найден");
        }
        educationalClassRepository.deleteById(id);
    }
}
