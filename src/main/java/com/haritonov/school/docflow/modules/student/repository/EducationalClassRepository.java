package com.haritonov.school.docflow.modules.student.repository;

import com.haritonov.school.docflow.modules.student.model.EducationalClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalClassRepository extends JpaRepository<EducationalClass, Long> {
}
