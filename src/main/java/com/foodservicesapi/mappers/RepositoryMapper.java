package com.foodservicesapi.mappers;

import com.foodservicesapi.models.domain.PairedRestaurantOverview;
import com.foodservicesapi.models.domain.RestaurantOverview;
import com.foodservicesapi.models.repositories.Restaurant;
import com.foodservicesapi.models.repositories.RestaurantServiceProvider;
import com.foodservicesapi.models.repositories.RestaurantsSearchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface RepositoryMapper {
  /*************************************** START - DOMAIN TO REPOSITORY ******************************************************/


  @Mappings({
          @Mapping(source = "restaurantList", target = "restaurantList"),
          @Mapping(source = "userIP", target = "userIP"),
          @Mapping(source = "address", target = "address"),
          @Mapping(source = "searchQuery", target = "searchQuery"),
  })
  RestaurantsSearchResult toRestaurantsSearchResult(
          Integer dummyInteger,
          List<Restaurant> restaurantList,
          String searchQuery,
          String address,
          String userIP
  );

  List<Restaurant> toRestaurantListRepository(
          List<PairedRestaurantOverview> pairedRestaurantOverview);

  @Mappings({@Mapping(source = "serviceProviderRestaurants", target = "serviceProviders")})
  Restaurant toRestaurantRepository(PairedRestaurantOverview pairedRestaurantOverview);

  List<RestaurantServiceProvider> toRestaurantServiceProviderListRepository(
      List<RestaurantOverview> restaurantOverviewList);

  @Mappings({@Mapping(source = "id", target = "serviceProviderRestaurantId")})
  RestaurantServiceProvider toRestaurantServiceProviderRepository(
      RestaurantOverview restaurantOverview);

  /**************************************** END - DOMAIN TO REPOSITORY ******************************************************/

  /*************************************** START - REPOSITORY TO DOMAIN ******************************************************/

  @Mappings({@Mapping(source = "serviceProviders", target = "serviceProviderRestaurants")})
  PairedRestaurantOverview toPairedRestaurantOverviewDomain(Restaurant restaurant);

  @Mappings({@Mapping(source = "serviceProviderRestaurantId", target = "id")})
  RestaurantOverview toRestaurantOverviewDomain(RestaurantServiceProvider restaurant);

  /*************************************** END - REPOSITORY TO DOMAIN ******************************************************/
}
