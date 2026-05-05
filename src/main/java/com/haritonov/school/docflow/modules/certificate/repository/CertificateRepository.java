package com.haritonov.school.docflow.modules.certificate.repository;

import com.haritonov.school.docflow.modules.certificate.model.CertificateOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateOfStudy, Long> {

}
