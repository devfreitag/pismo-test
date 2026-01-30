package com.devfreitag.pismotest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationTypeEnum {
    PURCHASE(1L),
    INSTALLMENT_PURCHASE(2L),
    WITHDRAWAL(3L),
    PAYMENT(4L);

    private final long code;

    public static OperationTypeEnum fromCode(long code) {
        for (OperationTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid OperationType code: " + code);
    }
}
