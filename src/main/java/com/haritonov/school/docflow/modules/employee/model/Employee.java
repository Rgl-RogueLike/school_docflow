package com.haritonov.school.docflow.modules.employee.model;

import com.haritonov.school.docflow.modules.user.model.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_employment")
    private LocalDate dateOfEmployment;

    @OneToOne(mappedBy = "employee")
    private AppUser user;

    @ManyToOne
    private Position position;
}
