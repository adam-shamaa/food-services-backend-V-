package com.foodservicesapi.models.repositories;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class RestaurantsSearchResult {
    @Id private String id;
    @Version private Long version;
    @CreatedDate private Date createdAt;
    private String userIP;
    List<Restaurant> restaurantList;
}
