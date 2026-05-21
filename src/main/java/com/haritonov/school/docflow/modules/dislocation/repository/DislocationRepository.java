package com.haritonov.school.docflow.modules.dislocation.repository;

import com.haritonov.school.docflow.modules.dislocation.model.OrderOfTemporaryDislocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DislocationRepository extends JpaRepository<OrderOfTemporaryDislocation, Long> {

}
