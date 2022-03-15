package com.foodservicesapi.models.domain;

import lombok.*;

@Data
@RequiredArgsConstructor
public class RestaurantMetadata {
  protected String id;
  protected String restaurantName;
  protected String serviceProviderName;
  protected String formattedAddress;
  protected String redirectUrl;
  protected double rating;
  protected int minEstimatedDeliveryTime;
  protected int maxEstimatedDeliveryTime;
  protected String imageUrl;
}
