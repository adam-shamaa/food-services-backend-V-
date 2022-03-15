package com.foodservicesapi.services.skipthedishes;

import com.foodservicesapi.mappers.SkipTheDishesMapper;
import com.foodservicesapi.models.domain.Address;
import com.foodservicesapi.models.domain.Restaurant;
import com.foodservicesapi.models.domain.RestaurantOverview;
import com.foodservicesapi.services.ServiceProvider;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class SkipTheDishesService implements ServiceProvider {

  private final SkipTheDishesClient skipTheDishesClient;
  private final SkipTheDishesMapper skipTheDishesMapper =
      Mappers.getMapper(SkipTheDishesMapper.class);

  @Override
  public CompletableFuture<List<RestaurantOverview>> retrieveRestaurants(
      Address address, String query) {
    return CompletableFuture.completedFuture(
        skipTheDishesMapper.toRestaurantOverviewDomainList(
            skipTheDishesClient
                .fetchRestaurantsLists(skipTheDishesMapper.toRestaurantsListRequest(address), query)
                .getData()
                .getRestaurantsList()
                .getOpenRestaurants()));
  }

  @Override
  public CompletableFuture<Restaurant> retrieveRestaurant(String id, Address address) {
    return CompletableFuture.completedFuture(
        skipTheDishesMapper.toRestaurantDomain(
            skipTheDishesClient.fetchRestaurantDetails(
                skipTheDishesMapper.toRestaurantDetailsRequest(address, id))));
  }
}
