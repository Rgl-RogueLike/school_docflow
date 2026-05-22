package com.haritonov.school.docflow.modules.document.repository;

import com.haritonov.school.docflow.model.base.DocumentCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentCounterRepository extends JpaRepository<DocumentCounter, Long> {

    Optional<DocumentCounter> findByPrefixAndYear(String prefix, Integer year);
}
