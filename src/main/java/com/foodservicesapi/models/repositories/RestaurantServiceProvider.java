package com.foodservicesapi.models.repositories;

import com.foodservicesapi.models.domain.enums.ServiceProviderEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantServiceProvider {
  private String serviceProviderRestaurantId;
  private ServiceProviderEnum serviceProvider;
}
