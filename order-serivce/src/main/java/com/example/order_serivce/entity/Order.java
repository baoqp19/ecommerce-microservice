package com.example.order_serivce.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.persistence.*;

import com.example.order_serivce.constrant.AppConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"cart"})
@Data
@Builder
@Entity
@Table(name = "orders")
public final class Order extends AbstractMappedEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false, updatable = false)
    private Integer orderId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = AppConstant.LOCAL_DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = AppConstant.LOCAL_DATE_TIME_FORMAT)
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_desc")
    private String orderDesc;

    @Column(name = "order_fee", columnDefinition = "decimal")
    private Double orderFee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
