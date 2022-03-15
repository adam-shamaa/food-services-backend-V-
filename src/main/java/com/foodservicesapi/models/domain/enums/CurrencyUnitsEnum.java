package com.foodservicesapi.models.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CurrencyUnitsEnum {
  DOLLARS("Dollars", 1),
  CENTS("Cents", 100);

  private final String formattedName;
  private final double baseTenRepresentation;

  CurrencyUnitsEnum(String formattedName, double baseTenRepresentation) {
    this.formattedName = formattedName;
    this.baseTenRepresentation = baseTenRepresentation;
  }

  public CurrencyUnitsEnum fromName(String name) {
    return Arrays.stream(CurrencyUnitsEnum.values())
        .filter(currencyUnitsEnum -> currencyUnitsEnum.getFormattedName().equals(name))
        .findFirst()
        .get();
  }
}
