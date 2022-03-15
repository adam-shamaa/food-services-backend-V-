package com.foodservicesapi.models.domain.enums;

import lombok.Getter;

@Getter
public enum FeeTypeEnum {
  DELIVERY("Delivery"),
  SERVICE("Service"),
  SMALL_ORDER("Small Order");

  private final String formattedName;

  FeeTypeEnum(String formattedName) {
    this.formattedName = formattedName;
  }
}
