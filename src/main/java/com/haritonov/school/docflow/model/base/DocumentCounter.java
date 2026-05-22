package com.haritonov.school.docflow.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "document_counters")
public class DocumentCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefix", nullable = false, unique = true)
    private String prefix;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "current_number", nullable = false)
    private Long currentNumber;

    public DocumentCounter(String prefix, Integer year, Long currentNumber) {
        this.prefix = prefix;
        this.year = year;
        this.currentNumber = currentNumber;
    }
}