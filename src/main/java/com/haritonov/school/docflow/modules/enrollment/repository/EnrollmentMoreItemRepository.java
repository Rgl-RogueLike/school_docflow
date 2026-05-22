package com.haritonov.school.docflow.modules.enrollment.repository;

import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollmentMoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentMoreItemRepository extends JpaRepository<OrderOfEnrollmentMoreItem, Long> {


}