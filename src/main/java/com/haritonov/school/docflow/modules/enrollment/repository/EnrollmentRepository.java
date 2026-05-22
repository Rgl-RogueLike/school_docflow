package com.haritonov.school.docflow.modules.enrollment.repository;

import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<OrderOfEnrollment, Long> {

}