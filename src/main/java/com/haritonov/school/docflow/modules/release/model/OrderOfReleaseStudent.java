package com.haritonov.school.docflow.modules.release.model;

import com.haritonov.school.docflow.model.base.Document;
import com.haritonov.school.docflow.modules.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_of_release_student")
public class OrderOfReleaseStudent extends Document {

    @Column(name = "based")
    private String basis;

    @Column(name = "basis_document_date")
    private LocalDate basisDocumentDate;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_on")
    private LocalDate dateOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
