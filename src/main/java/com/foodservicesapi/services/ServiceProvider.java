package com.foodservicesapi.services;

import com.foodservicesapi.models.domain.Address;
import com.foodservicesapi.models.domain.Restaurant;
import com.foodservicesapi.models.domain.RestaurantOverview;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ServiceProvider {
  CompletableFuture<List<RestaurantOverview>> retrieveRestaurants(Address address, String query);

  CompletableFuture<Restaurant> retrieveRestaurant(String id, Address address);
}
