package com.foodservicesapi.models.repositories;

import com.foodservicesapi.models.domain.enums.ServiceProviderEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

@Data
@Builder
public class RestaurantServiceProvider {
  private String serviceProviderRestaurantId;
  private ServiceProviderEnum serviceProvider;
}
