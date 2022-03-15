package com.foodservicesapi.models.repositories;

import com.foodservicesapi.models.domain.enums.ServiceProviderEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Builder
public class RestaurantServiceProvider {
  private String serviceProviderRestaurantId;
  private ServiceProviderEnum serviceProvider;
  @CreatedDate private Date createdAt;
}
