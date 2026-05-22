package com.haritonov.school.docflow.modules.student.repository;

import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.dto.StudentFilterDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    List<Student> findByEducationalClass_Id(Long classId);

    static Specification<Student> withFilter(StudentFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(filter.getLastName())) {
                predicates.add(cb.like(root.get("lastName"), "%" + filter.getLastName() + "%"));
            }

            if (StringUtils.isNotBlank(filter.getFirstName())) {
                predicates.add(cb.like(root.get("firstName"), "%" + filter.getFirstName() + "%"));
            }

            if (StringUtils.isNotBlank(filter.getPatronymic())) {
                predicates.add(cb.like(root.get("patronymic"), "%" + filter.getPatronymic() + "%"));
            }

            if (StringUtils.isNotBlank(filter.getGender())) {
                predicates.add(cb.equal(root.get("gender"), filter.getGender()));
            }

            if (StringUtils.isNotBlank(filter.getClassName())) {
                predicates.add(cb.like(root.get("educationalClass").get("name"), "%" + filter.getClassName() + "%"));
            }

            if (filter.getDateOfBirthFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), filter.getDateOfBirthFrom()));
            }

            if (filter.getDateOfBirthTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOfBirth"), filter.getDateOfBirthTo()));
            }

            if (filter.getDateOfEnrollmentFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfEnrollment"), filter.getDateOfEnrollmentFrom()));
            }

            if (filter.getDateOfEnrollmentTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOfEnrollment"), filter.getDateOfEnrollmentTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}