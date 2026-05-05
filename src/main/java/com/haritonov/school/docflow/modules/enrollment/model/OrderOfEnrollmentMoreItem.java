package com.haritonov.school.docflow.modules.enrollment.model;

import com.haritonov.school.docflow.modules.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_of_enrollment_more_table")
public class OrderOfEnrollmentMoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_enrollment_more_id", nullable = false)
    private OrderOfEnrollmentMore order;
}
