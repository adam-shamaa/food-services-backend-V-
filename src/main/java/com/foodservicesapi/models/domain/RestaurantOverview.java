package com.foodservicesapi.models.domain;

import com.foodservicesapi.models.domain.enums.ServiceProviderEnum;
import lombok.Data;

@Data
public class RestaurantOverview {
    private String id;
    private String name;
    private double rating;
    private String imageUrl;
    private ServiceProviderEnum serviceProvider;
}
