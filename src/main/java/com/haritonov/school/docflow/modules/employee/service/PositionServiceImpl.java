package com.haritonov.school.docflow.modules.employee.service;

import com.haritonov.school.docflow.modules.employee.dto.PositionCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.PositionResponse;
import com.haritonov.school.docflow.modules.employee.dto.PositionUpdateRequest;
import com.haritonov.school.docflow.modules.employee.model.Position;
import com.haritonov.school.docflow.modules.employee.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService{

    private final PositionRepository positionRepository;

    private PositionResponse mapToResponse(Position entity) {
        PositionResponse response = new PositionResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        return response;
    }

    @Override
    @Transactional
    public Long create(PositionCreateRequest request) {
        Position position = new Position();
        position.setName(request.getName());

        Position savedPosition = positionRepository.save(position);
        return savedPosition.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponse> getAll() {
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponse getById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Должность не найдена"));
        return mapToResponse(position);
    }

    @Override
    @Transactional
    public void update(PositionUpdateRequest request) {
        Position position = positionRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Должность не найдена"));
        position.setName(request.getName());
        positionRepository.save(position);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new IllegalArgumentException("Должность не найдена");
        }
        positionRepository.deleteById(id);
    }
}
