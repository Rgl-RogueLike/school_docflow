package com.haritonov.school.docflow.modules.certificate.repository;

import com.haritonov.school.docflow.modules.certificate.model.Certificate;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateFilterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {

    static Specification<Certificate> withFilter(CertificateFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(filter.getDocumentNumber())) {
                predicates.add(cb.like(root.get("documentNumber"), "%" + filter.getDocumentNumber() + "%"));
            }
            if (filter.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("documentDate"), filter.getDateFrom().atStartOfDay()));
            }
            if (filter.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("documentDate"), filter.getDateTo().atTime(23, 59, 59)));
            }
            if (StringUtils.isNotBlank(filter.getStudentName())) {
                predicates.add(cb.like(
                        cb.concat(cb.concat(root.get("student").get("lastName"), " "),
                                cb.concat(root.get("student").get("firstName"), " ")),
                        "%" + filter.getStudentName() + "%"));
            }
            if (StringUtils.isNotBlank(filter.getCreatorName())) {
                predicates.add(cb.like(
                        cb.concat(cb.concat(root.get("creator").get("lastName"), " "),
                                cb.concat(root.get("creator").get("firstName"), " ")),
                        "%" + filter.getCreatorName() + "%"));
            }
            if (StringUtils.isNotBlank(filter.getClassName())) {
                predicates.add(cb.like(root.get("student").get("educationalClass").get("name"), "%" + filter.getClassName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}