package com.haritonov.school.docflow.modules.dislocation.model;

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
@Table(name = "order_of_temporary_dislocation")
public class OrderOfTemporaryDislocation extends Document {

    @Column(name = "in_relation")
    private String relation;

    @Column(name = "based")
    private String basis;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "effective_start_date")
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
