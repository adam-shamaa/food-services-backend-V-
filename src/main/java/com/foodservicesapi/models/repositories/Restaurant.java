package com.foodservicesapi.models.repositories;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Restaurant {
  @Id private String id;
  @Version private Long version;
  @CreatedDate private Date createdAt;
  @LastModifiedDate private Date lastModifiedDate;

  private List<RestaurantServiceProvider> serviceProviders;
}
