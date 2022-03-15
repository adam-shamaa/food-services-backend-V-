package com.foodservicesapi.models.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PairedRestaurantOverview {
  private String id;
  private List<RestaurantOverview> serviceProviderRestaurants;
}
