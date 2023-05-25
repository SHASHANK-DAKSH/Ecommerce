package com.example.MainProject.entities.Orders;

import com.example.MainProject.enums.FromStatus;
import com.example.MainProject.enums.ToStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OrderStatus {
    @Id
    @OneToOne
    private OrdersProduct ordersProduct;

    @Enumerated(EnumType.STRING)
    private FromStatus fromStatus;
    @Enumerated(EnumType.STRING)
    private ToStatus toStatus;
    private String transitionNotesComments;
    private Date transitionDate;
}
