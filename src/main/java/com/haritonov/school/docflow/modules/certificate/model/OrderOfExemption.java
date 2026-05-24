package com.haritonov.school.docflow.modules.certificate.model;

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
@Table(name = "certificate_of_study")
public class OrderOfExemption extends Document {

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "purpose")
    private String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
}
