package com.foodservicesapi.repositories;

import com.foodservicesapi.models.repositories.RestaurantsSearchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RestaurantsSearchRepository extends MongoRepository<RestaurantsSearchResult, String> {
    @Query(value = "{ 'restaurantList.$_id' : ?0 }")
    List<RestaurantsSearchResult> findByRestaurantList_Id(final String Id);
}
