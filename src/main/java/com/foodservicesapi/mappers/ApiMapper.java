package com.foodservicesapi.mappers;

import com.foodservices.apicodegen.model.MenuItem;
import com.foodservices.apicodegen.model.RestaurantAggregate;
import com.foodservices.apicodegen.model.RestaurantPreview;
import com.foodservices.apicodegen.model.ServiceProviderRestaurant;
import com.foodservicesapi.models.domain.*;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ApiMapper {

  /*************************************** START - DTO TO DOMAIN ******************************************************/

  Address toAddressDomain(com.foodservices.apicodegen.model.Address address);

  /**************************************** END - DTO TO DOMAIN ******************************************************/


  /*************************************** START - DOMAIN TO DTO ******************************************************/

  // ------------------------------------ START -  TO RestaurantPreviewDTO ---------------------------------------------
  List<RestaurantPreview> toRestaurantPreviewListDTO(List<PairedRestaurantOverview> pairedRestaurantOverviewList);

  @Mappings({
    @Mapping(source = "id", target = "id"),
    @Mapping(source = "serviceProviderRestaurants", target = "name", qualifiedByName = "firstRestaurantOverviewName"),
    @Mapping(source = "serviceProviderRestaurants", target = "imageUrl", qualifiedByName = "firstRestaurantOverviewImageUrl"),
    @Mapping(source = "serviceProviderRestaurants", target = "averageRating", qualifiedByName = "restaurantOverviewRating")
  })
  RestaurantPreview toRestaurantPreviewDTO(PairedRestaurantOverview pairedRestaurantOverview);

  @Named("firstRestaurantOverviewName")
  default String firstRestaurantOverviewName(List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList != null && !restaurantOverviewList.isEmpty() ? restaurantOverviewList.get(0).getName() : null;
  }
  @Named("firstRestaurantOverviewImageUrl")
  default String firstRestaurantOverviewImageUrl(List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList != null && !restaurantOverviewList.isEmpty() ? restaurantOverviewList.get(0).getImageUrl() : null;
  }
  @Named("restaurantOverviewRating")
  default double restaurantOverviewRating(List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList.stream().mapToDouble(RestaurantOverview::getRating).sum()/restaurantOverviewList.size();
  }

  // ------------------------------------ END - TO RestaurantPreviewDTO -----------------------------------------------

  // ------------------------------------ START - TO RestaurantAggregateDTO --------------------------------------------

  @Mappings({
    @Mapping(source = "restaurantList", target = "serviceProviders"),
    @Mapping(source = "restaurantList", target = "name", qualifiedByName = "firstRestaurantName"),
    @Mapping(source = "restaurantList", target = "imageUrl", qualifiedByName = "firstImageUrl"),
    @Mapping(source = "restaurantList", target = "formattedAddress", qualifiedByName = "firstAddress")
  })
  RestaurantAggregate toRestaurantAggregateDTO(Integer dummyValueIgnore, List<Restaurant> restaurantList, @Context double subtotal);

  @Mappings({
          @Mapping(source = "fees", target = "fees", qualifiedByName = "toFeesListDTO"),
          @Mapping(source = "menu", target = "menu")
  })
  ServiceProviderRestaurant toServiceProviderDTO(Restaurant restaurant, @Context double subtotal);

  @Mappings({
    @Mapping(source = "price", target = "magnitude"),
    @Mapping(source = "priceUnits", target = "magnitudeUnit")
  })
  MenuItem toMenuItemDTO(com.foodservicesapi.models.domain.MenuItem menuItem);

  @Mapping(source = "fee", target = "magnitude", qualifiedByName = "feeMagnitude")
  com.foodservices.apicodegen.model.Fee toFeeDTO(Fee fee, @Context double subtotal);

  @Named("firstRestaurantName")
  default String firstRestaurantName(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty()) ? restaurantList.get(0).getRestaurantName() : null;
  }
  @Named("firstImageUrl")
  default String firstImageUrl(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty()) ? restaurantList.get(0).getImageUrl() : null;
  }
  @Named("firstAddress")
  default String firstAddress(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty()) ? restaurantList.get(0).getFormattedAddress() : null;
  }
  @Named("toFeesListDTO")
  default List<com.foodservices.apicodegen.model.Fee> toFeesListDTO(List<Fee> fees, @Context double subtotal) {
    return fees.stream().filter(fee -> subtotal >= fee.getMinimumCartSubtotal() && subtotal < fee.getMaximumCartSubtotal()).map(fee -> toFeeDTO(fee, subtotal)).collect(Collectors.toList());
  }
  @Named("feeMagnitude")
  default double fee(Fee fee, @Context double subtotal) {
    double expectedMagnitude = fee.isPercent() ? subtotal * fee.getMagnitude() : fee.getMagnitude();
    expectedMagnitude = expectedMagnitude > fee.getMaximumFeeMagnitude() ? fee.getMaximumFeeMagnitude() : expectedMagnitude;
    expectedMagnitude = expectedMagnitude < fee.getMinimumFeeMagnitude() ? fee.getMinimumFeeMagnitude() : expectedMagnitude;
    return expectedMagnitude;
  }
  // ------------------------------------ END - TO RestaurantPreviewDTO -----------------------------------------------

  /**************************************** END - DOMAIN TO DTO ******************************************************/
}