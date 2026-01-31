package com.devfreitag.pismotest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class OperationType {

    @Id
    @Column(name = "operation_type_id", unique = true, nullable = false)
    private Long operationTypeId;

    @Column(nullable = false)
    private String description;
}
