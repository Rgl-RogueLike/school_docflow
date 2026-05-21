package com.haritonov.school.docflow.modules.student.repository;

import com.haritonov.school.docflow.modules.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByEducationalClass_Id(Long classId);
}
