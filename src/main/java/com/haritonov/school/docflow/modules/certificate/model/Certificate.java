package com.haritonov.school.docflow.modules.certificate.model;

import com.haritonov.school.docflow.model.base.Document;
import com.haritonov.school.docflow.modules.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "certificates")
public class Certificate extends Document {

    @Column(name = "academic_year", length = 20)
    private String academicYear; // например "2025-2026"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}