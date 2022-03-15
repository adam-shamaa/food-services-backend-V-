package com.foodservicesapi.mappers;

import com.foodservices.apicodegen.model.*;
import com.foodservicesapi.models.domain.*;
import com.foodservicesapi.models.domain.enums.CurrencyUnitsEnum;
import com.foodservicesapi.services.RestaurantUtils;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Mapper
public interface ApiMapper {

  /*************************************** START - DTO TO DOMAIN ******************************************************/

  Address toAddressDomain(com.foodservices.apicodegen.model.AddressRequest address);

  /**************************************** END - DTO TO DOMAIN ******************************************************/

  /*************************************** START - DOMAIN TO DTO ******************************************************/

  // ------------------------------------ START -  TO RestaurantPreviewDTO ---------------------------------------------
  List<SummaryRestaurantResponse> toRestaurantPreviewListDTO(
      List<PairedRestaurantOverview> pairedRestaurantOverviewList);

  @Mappings({
    @Mapping(source = "id", target = "id"),
    @Mapping(
        source = "serviceProviderRestaurants",
        target = "name",
        qualifiedByName = "firstRestaurantOverviewName"),
    @Mapping(
        source = "serviceProviderRestaurants",
        target = "imageUrl",
        qualifiedByName = "firstRestaurantOverviewImageUrl"),
    @Mapping(
        source = "serviceProviderRestaurants",
        target = "averageRating",
        qualifiedByName = "restaurantOverviewRating"),
    @Mapping(
        source = "serviceProviderRestaurants",
        target = "minEstimatedDeliveryTime",
        qualifiedByName = "minEstimatedDeliveryTimeRestaurantOverview"),
    @Mapping(
        source = "serviceProviderRestaurants",
        target = "maxEstimatedDeliveryTime",
        qualifiedByName = "maxEstimatedDeliveryTimeRestaurantOverview")
  })
  SummaryRestaurantResponse toRestaurantPreviewDTO(
      PairedRestaurantOverview pairedRestaurantOverview);

  @Named("minEstimatedDeliveryTimeRestaurantOverview")
  default int minEstimatedDeliveryTimeRestaurantOverview(
      List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList.stream()
        .mapToInt(RestaurantOverview::getMinEstimatedDeliveryTimeMinutes)
        .min()
        .orElse(-1);
  }

  @Named("maxEstimatedDeliveryTimeRestaurantOverview")
  default int maxEstimatedDeliveryTimeRestaurantOverview(
      List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList.stream()
        .mapToInt(RestaurantOverview::getMinEstimatedDeliveryTimeMinutes)
        .max()
        .orElse(-1);
  }

  @Named("firstRestaurantOverviewName")
  default String firstRestaurantOverviewName(List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList != null && !restaurantOverviewList.isEmpty()
        ? restaurantOverviewList.get(0).getName()
        : null;
  }

  @Named("firstRestaurantOverviewImageUrl")
  default String firstRestaurantOverviewImageUrl(List<RestaurantOverview> restaurantOverviewList) {
    return restaurantOverviewList != null && !restaurantOverviewList.isEmpty()
        ? restaurantOverviewList.get(0).getImageUrl()
        : null;
  }

  @Named("restaurantOverviewRating")
  default double restaurantOverviewRating(List<RestaurantOverview> restaurantOverviewList) {
    return Math.round(
            (restaurantOverviewList.stream().mapToDouble(RestaurantOverview::getRating).sum()
                    / restaurantOverviewList.size())
                * 100.0)
        / 100.0;
  }

  // ------------------------------------ END - TO RestaurantPreviewDTO -----------------------------------------------

  // ------------------------------------ START - TO RestaurantAggregateDTO --------------------------------------------

  @Mappings({
    @Mapping(
        source = "restaurantList",
        target = "serviceProviders",
        qualifiedByName = "toServiceProvidersList"),
    @Mapping(source = "restaurantList", target = "name", qualifiedByName = "firstRestaurantName"),
    @Mapping(source = "restaurantList", target = "imageUrl", qualifiedByName = "firstImageUrl"),
    @Mapping(
        source = "restaurantList",
        target = "formattedAddress",
        qualifiedByName = "firstAddress"),
    @Mapping(
        source = "restaurantList",
        target = "minEstimatedDeliveryTime",
        qualifiedByName = "minEstimatedDeliveryTimeRestaurantDetails"),
    @Mapping(
        source = "restaurantList",
        target = "maxEstimatedDeliveryTime",
        qualifiedByName = "maxEstimatedDeliveryTimeRestaurantDetails")
  })
  DetailedRestaurantResponse toRestaurantAggregateDTO(
      Integer dummyValueIgnore, List<Restaurant> restaurantList, @Context double subtotal);

  @Named("toServiceProvidersList")
  default List<ServiceProviderRestaurantResponse> toServiceProvidersList(
      List<Restaurant> restaurantList, @Context double subtotal) {
    List<ServiceProviderRestaurantResponse> mappedServiceProvidersList =
        restaurantList.stream()
            .map(restaurant -> this.toServiceProviderDTO(restaurant, subtotal))
            .collect(Collectors.toList());

    Map<ServiceProviderNameEnum, List<ServiceProviderRestaurantResponse>>
        serviceProvidersGroupedByServiceProvider =
            mappedServiceProvidersList.stream()
                .collect(groupingBy(ServiceProviderRestaurantResponse::getServiceProviderName));

    List<ServiceProviderRestaurantResponse> cheapestRestaurantForEachServiceProviderList =
        new ArrayList<>();

    for (List<ServiceProviderRestaurantResponse> serviceProvidersList :
        serviceProvidersGroupedByServiceProvider.values()) {
      cheapestRestaurantForEachServiceProviderList.add(
          cheapestServiceProvider(serviceProvidersList));
    }

    return cheapestRestaurantForEachServiceProviderList;
  }

  @Mappings({
    @Mapping(source = "fees", target = "fees", qualifiedByName = "toFeesListDTO"),
    @Mapping(source = "menu", target = "menu")
  })
  ServiceProviderRestaurantResponse toServiceProviderDTO(
      Restaurant restaurant, @Context double subtotal);

  @Mappings({
    @Mapping(source = "price", target = "magnitude"),
    @Mapping(source = "priceUnits", target = "magnitudeUnit")
  })
  MenuItemResponse toMenuItemDTO(com.foodservicesapi.models.domain.MenuItem menuItem);

  @Mapping(source = "fee", target = "magnitude", qualifiedByName = "feeMagnitude")
  com.foodservices.apicodegen.model.FeeResponse toFeeDTO(Fee fee, @Context double subtotal);

  @AfterMapping
  default void cheapestServiceProviderMapping(
      @MappingTarget DetailedRestaurantResponse detailedRestaurantResponse) {
    detailedRestaurantResponse.setCheapestServiceProvider(
        cheapestServiceProvider(detailedRestaurantResponse.getServiceProviders())
            .getServiceProviderName());
  }

  default ServiceProviderRestaurantResponse cheapestServiceProvider(
      List<ServiceProviderRestaurantResponse> serviceProviders) {

    ServiceProviderRestaurantResponse cheapestServiceProvider = serviceProviders.get(0);

    double cheapestServiceProviderTotalFees = Double.MAX_VALUE;

    for (ServiceProviderRestaurantResponse serviceProvider : serviceProviders) {
      double totalFees =
          serviceProvider.getFees().stream()
              .mapToDouble(
                  fee ->
                      RestaurantUtils.toMagnitudeUnits(
                          fee.getMagnitude().doubleValue(),
                          CurrencyUnitsEnum.valueOf(fee.getMagnitudeUnits().name()),
                          CurrencyUnitsEnum.CENTS))
              .sum();

      if (totalFees < cheapestServiceProviderTotalFees) {
        cheapestServiceProvider = serviceProvider;
        cheapestServiceProviderTotalFees = totalFees;
      }
    }

    return cheapestServiceProvider;
  }

  @Named("minEstimatedDeliveryTimeRestaurantDetails")
  default int minEstimatedDeliveryTimeRestaurantDetails(List<Restaurant> restaurantList) {
    return restaurantList.stream()
        .mapToInt(Restaurant::getMinEstimatedDeliveryTime)
        .min()
        .orElse(-1);
  }

  @Named("maxEstimatedDeliveryTimeRestaurantDetails")
  default int maxEstimatedDeliveryTimeRestaurantDetails(List<Restaurant> restaurantList) {
    return restaurantList.stream()
        .mapToInt(Restaurant::getMaxEstimatedDeliveryTime)
        .max()
        .orElse(-1);
  }

  @Named("firstRestaurantName")
  default String firstRestaurantName(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty())
        ? restaurantList.get(0).getRestaurantName()
        : null;
  }

  @Named("firstImageUrl")
  default String firstImageUrl(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty())
        ? restaurantList.get(0).getImageUrl()
        : null;
  }

  @Named("firstAddress")
  default String firstAddress(List<Restaurant> restaurantList) {
    return (restaurantList != null && !restaurantList.isEmpty())
        ? restaurantList.get(0).getFormattedAddress()
        : null;
  }

  @Named("toFeesListDTO")
  default List<com.foodservices.apicodegen.model.FeeResponse> toFeesListDTO(
      List<Fee> fees, @Context double subtotal) {
    return fees.stream()
        .filter(
            fee ->
                subtotal >= fee.getMinimumCartSubtotal() && subtotal < fee.getMaximumCartSubtotal())
        .map(fee -> toFeeDTO(fee, subtotal))
        .collect(Collectors.toList());
  }

  @Named("feeMagnitude")
  default double fee(Fee fee, @Context double subtotal) {
    double expectedMagnitude = fee.isPercent() ? subtotal * fee.getMagnitude() : fee.getMagnitude();
    expectedMagnitude =
        expectedMagnitude > fee.getMaximumFeeMagnitude()
            ? fee.getMaximumFeeMagnitude()
            : expectedMagnitude;
    expectedMagnitude =
        expectedMagnitude < fee.getMinimumFeeMagnitude()
            ? fee.getMinimumFeeMagnitude()
            : expectedMagnitude;
    return expectedMagnitude;
  }
  // ------------------------------------ END - TO RestaurantPreviewDTO
  // -----------------------------------------------

  /**************************************** END - DOMAIN TO DTO ******************************************************/
}
