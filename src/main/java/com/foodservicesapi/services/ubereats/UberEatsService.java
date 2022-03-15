package com.foodservicesapi.services.ubereats;

import com.foodservicesapi.codegen.models.RestaurantsListResponseFeedItemUbereats;
import com.foodservicesapi.mappers.UberEatsMapper;
import com.foodservicesapi.models.domain.Address;
import com.foodservicesapi.models.domain.Restaurant;
import com.foodservicesapi.models.domain.RestaurantOverview;
import com.foodservicesapi.services.ServiceProvider;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UberEatsService implements ServiceProvider {
  private final UberEatsClient uberEatsClient;
  private final UberEatsMapper uberEatsMapper = Mappers.getMapper(UberEatsMapper.class);

  @Async
  @Override
  public CompletableFuture<List<RestaurantOverview>> retrieveRestaurants(
      Address address, String searchQuery) {
    Set<String> restaurantId = new HashSet<>();
    return CompletableFuture.completedFuture(
        uberEatsMapper
            .restaurantsOverviewsToRestaurantOverviewsDomain(
                uberEatsClient
                    .fetchRestaurantsLists(
                        uberEatsMapper.toRestaurantsListsRequestCookie(address),
                        uberEatsMapper.toRestaurantsListsRequestBody(searchQuery))
                    .getData()
                    .getFeedItems()
                    .stream()
                    .map(RestaurantsListResponseFeedItemUbereats::getStore)
                    .filter(Objects::nonNull)
                    .filter(restaurant -> restaurant.getImageOverlay() == null)
                    .collect(Collectors.toList()))
            .stream()
            .filter(r -> restaurantId.add(r.getId()))
            .collect(Collectors.toList()));
  }

  @Override
  public CompletableFuture<Restaurant> retrieveRestaurant(String id, Address address) {
    return CompletableFuture.completedFuture(
        uberEatsMapper.toRestaurantDomain(
            uberEatsClient.fetchRestaurantDetails(
                uberEatsMapper.toRestaurantDetailsRequestCookie(address),
                uberEatsMapper.toRestaurantDetailsRequestBody(id))));
  }
}
