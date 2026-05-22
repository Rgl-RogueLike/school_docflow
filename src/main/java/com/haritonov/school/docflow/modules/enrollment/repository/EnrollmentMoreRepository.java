package com.haritonov.school.docflow.modules.enrollment.repository;

import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollmentMore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentMoreRepository extends JpaRepository<OrderOfEnrollmentMore, Long> {

}