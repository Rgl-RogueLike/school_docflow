package com.haritonov.school.docflow.modules.release.repository;

import com.haritonov.school.docflow.modules.release.model.OrderOfReleaseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<OrderOfReleaseStudent, Long> {


}