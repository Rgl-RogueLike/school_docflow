package com.haritonov.school.docflow.modules.enrollment.model;

import com.haritonov.school.docflow.model.base.Document;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_of_enrollment_more")
public class OrderOfEnrollmentMore extends Document {

    @Column(name = "basis")
    private String basis;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderOfEnrollmentMoreItem> items = new ArrayList<>();

    public void addItem(OrderOfEnrollmentMoreItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderOfEnrollmentMoreItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
