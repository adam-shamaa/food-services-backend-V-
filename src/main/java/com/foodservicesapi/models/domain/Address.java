package com.foodservicesapi.models.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Address {
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private double latitude;
    private double longitude;
}
