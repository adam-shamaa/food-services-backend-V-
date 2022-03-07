package com.foodservicesapi.models.domain.enums;

import lombok.Getter;

@Getter
public enum ServiceProviderEnum {
    SKIPTHEDISHES("SkipTheDishes"),
    UBEREATS("UberEats"),
    DOORDASH("Doordash");

    private final String formattedName;

    ServiceProviderEnum(String formattedName) {
        this.formattedName = formattedName;
    }
}
