package com.example.payment_service.domain;


import lombok.*;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "payments")
public class Payment extends AbstractMappedEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", unique = true, nullable = false, updatable = false)
    private Integer paymentId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "is_payed")
    private Boolean isPayed;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

}
