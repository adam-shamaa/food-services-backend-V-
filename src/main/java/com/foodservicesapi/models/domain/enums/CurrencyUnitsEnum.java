package com.foodservicesapi.models.domain.enums;

import lombok.Getter;

@Getter
public enum CurrencyUnitsEnum {
    DOLLARS("Dollars"),
    CENTS("Cents");

    private final String formattedName;

    CurrencyUnitsEnum(String formattedName) {
        this.formattedName = formattedName;
    }
}
