package com.devfreitag.pismotest.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(unique = true, nullable = false)
    private String documentNumber;

    @Column(name = "available_credit_limit", nullable = false)
    private BigDecimal availableCreditLimit;

}
