package com.foodservicesapi.repositories;

import com.foodservicesapi.models.repositories.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {}
