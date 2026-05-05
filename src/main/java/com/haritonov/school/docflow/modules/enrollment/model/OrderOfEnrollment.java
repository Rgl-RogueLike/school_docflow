package com.haritonov.school.docflow.modules.enrollment.model;

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
@Table(name = "order_of_enrollment")
public class OrderOfEnrollment extends Document {

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "reason")
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

}
