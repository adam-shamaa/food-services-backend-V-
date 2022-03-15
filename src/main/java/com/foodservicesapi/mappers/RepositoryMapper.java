package com.foodservicesapi.mappers;

import com.foodservicesapi.models.domain.PairedRestaurantOverview;
import com.foodservicesapi.models.domain.RestaurantOverview;
import com.foodservicesapi.models.repositories.Restaurant;
import com.foodservicesapi.models.repositories.RestaurantServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface RepositoryMapper {
  /*************************************** START - DOMAIN TO REPOSITORY ******************************************************/

  List<RestaurantServiceProvider> toRestaurantServiceProviderListRepository(
      List<RestaurantOverview> restaurantOverviewList);

  @Mappings({@Mapping(source = "id", target = "serviceProviderRestaurantId")})
  RestaurantServiceProvider toRestaurantServiceProviderRepository(
      RestaurantOverview restaurantOverview);

  List<Restaurant> toRestaurantListRepository(
      List<PairedRestaurantOverview> pairedRestaurantOverview);

  @Mappings({@Mapping(source = "serviceProviderRestaurants", target = "serviceProviders")})
  Restaurant toRestaurantRepository(PairedRestaurantOverview pairedRestaurantOverview);

  /**************************************** END - DOMAIN TO REPOSITORY ******************************************************/

  /*************************************** START - REPOSITORY TO DOMAIN ******************************************************/

  @Mappings({@Mapping(source = "serviceProviders", target = "serviceProviderRestaurants")})
  PairedRestaurantOverview toPairedRestaurantOverviewDomain(Restaurant restaurant);

  @Mappings({@Mapping(source = "serviceProviderRestaurantId", target = "id")})
  RestaurantOverview toRestaurantOverviewDomain(RestaurantServiceProvider restaurant);

  /*************************************** END - REPOSITORY TO DOMAIN ******************************************************/
}
