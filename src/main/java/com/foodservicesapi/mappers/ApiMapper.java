package com.foodservicesapi.mappers;

import com.foodservices.apicodegen.model.*;
import com.foodservicesapi.models.domain.*;
import com.foodservicesapi.models.domain.enums.CurrencyUnitsEnum;
import com.foodservicesapi.services.RestaurantUtils;
import org.mapstruct.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Mapper
public interface ApiMapper {

  /*************************************** START - DTO TO DOMAIN ******************************************************/

  @Mappings({
          @Mapping(
                  source = "addressRequestDto.address",
                  target = "."
          )
  })
  Address toAddressDomain(AddressRequestDto addressRequestDto);

  /**************************************** END - DTO TO DOMAIN ******************************************************/

  /*************************************** START - DOMAIN TO DTO ******************************************************/

  // ------------------------------------ START -  TO RestaurantPreviewDTO ---------------------------------------------
  @Mappings({
          @Mapping(
                  source = "restaurantSummaryDtoList",
                  target = "availableRestaurants"
          )
  })
  RestaurantSummarysResponseDto toRestaurantSummarysResponseDTO(
          Integer ignoreDummyIntegerValue,
          List<RestaurantSummaryDto> restaurantSummaryDtoList
  );

  List<RestaurantSummaryDto> toRestaurantPreviewListDTO(
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
  RestaurantSummaryDto toRestaurantPreviewDTO(
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

  // ------------------------------------ START - TO RestaurantDetailsDTO --------------------------------------------
  @Mappings({
          @Mapping(
                  source = "restaurantDetailsDto",
                  target = "restaurantDetails"
          )
  })
  RestaurantDetailsResponseDto toRestaurantDetailsResponseDTO(RestaurantDetailsDto restaurantDetailsDto);

  @Mappings({
    @Mapping(
        source = "restaurantList",
        target = "serviceProviders",
        qualifiedByName = "toServiceProviderNamesList"),
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
        qualifiedByName = "maxEstimatedDeliveryTimeRestaurantDetails"),
    @Mapping(
            source = "restaurantList",
            target = "menu",
            qualifiedByName = "firstMenuRestaurantDetails")
  })
  RestaurantDetailsDto toRestaurantDetailsDTO(
      Integer dummyValueIgnore, List<Restaurant> restaurantList);

  List<MenuCategoryDto> toMenuCategoryDtoList(List<MenuCategory> menuCategory);

  MenuCategoryDto toMenuCategoryDto(MenuCategory menuCategory);

  @Named("toServiceProviderNamesList")
  default List<ServiceProviderNameEnumDto> toServiceProviderNamesList(List<Restaurant> restaurantList) {
    return restaurantList.stream().map(
            restaurant -> Enum.valueOf( ServiceProviderNameEnumDto.class, restaurant.getServiceProviderName())
    ).distinct().collect(Collectors.toList());
  }

  @Named("firstMenuRestaurantDetails")
  default List<MenuCategoryDto> firstMenuRestaurantDetails(List<Restaurant> restaurantList) {
    return toMenuCategoryDtoList(restaurantList.get(0).getMenu());
  }


  // ------------------------------------ START - TO ServiceProvidersDTO --------------------------------------------

  @Mappings({
          @Mapping(
                  source = "restaurantList",
                  target = "serviceProviders",
                  qualifiedByName = "toServiceProvidersList"
          )
  })
  RestaurantServiceProvidersResponseDto toRestaurantServiceProvidersResponse(Integer dummyInteger,
          List<Restaurant> restaurantList, @Context double subtotal);

  @Named("toServiceProvidersList")
  default List<RestaurantServiceProviderDto> toServiceProvidersList(List<Restaurant> restaurantList, @Context double subtotal) {

    List<RestaurantServiceProviderDto> mappedServiceProvidersList =
        restaurantList.stream()
            .map(restaurant -> this.toServiceProviderDTO(restaurant, subtotal))
            .collect(Collectors.toList());

    Map<ServiceProviderNameEnumDto, List<RestaurantServiceProviderDto>>
        serviceProvidersGroupedByServiceProvider =
            mappedServiceProvidersList.stream()
                .collect(groupingBy(RestaurantServiceProviderDto::getServiceProviderName));

    List<RestaurantServiceProviderDto> cheapestRestaurantForEachServiceProviderList = new ArrayList<>();

    for (List<RestaurantServiceProviderDto> serviceProvidersList : serviceProvidersGroupedByServiceProvider.values()) {
      cheapestRestaurantForEachServiceProviderList.add(
          cheapestServiceProvider(serviceProvidersList));
    }

    return cheapestRestaurantForEachServiceProviderList;
  }

  @Mappings({
    @Mapping(source = "fees", target = "fees", qualifiedByName = "toFeesListDTO"),
  })
  RestaurantServiceProviderDto toServiceProviderDTO(
      Restaurant restaurant, @Context double subtotal);

  @Mappings({
    @Mapping(source = "price", target = "magnitude"),
    @Mapping(source = "priceUnits", target = "magnitudeUnit")
  })
  MenuItemDto toMenuItemDTO(com.foodservicesapi.models.domain.MenuItem menuItem);

  @Mapping(source = "fee", target = "magnitude", qualifiedByName = "feeMagnitude")
  com.foodservices.apicodegen.model.FeeDto toFeeDTO(Fee fee, @Context double subtotal);

  @AfterMapping
  default void cheapestServiceProviderMapping(@MappingTarget RestaurantServiceProvidersResponseDto restaurantServiceProvidersResponseDto) {
    restaurantServiceProvidersResponseDto
            .setCheapestServiceProvider(
                    cheapestServiceProvider(restaurantServiceProvidersResponseDto.getServiceProviders())
                    .getServiceProviderName()
            );
  }

  default RestaurantServiceProviderDto cheapestServiceProvider(List<RestaurantServiceProviderDto> serviceProviders) {
    RestaurantServiceProviderDto cheapestServiceProvider = serviceProviders.get(0);
    double cheapestServiceProviderTotalFees = Double.MAX_VALUE;

    for (RestaurantServiceProviderDto serviceProvider : serviceProviders) {
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
  default List<FeeDto> toFeesListDTO(List<Fee> fees, @Context double subtotal) {
    return fees.stream()
        .filter(
            fee ->
                subtotal >= fee.getMinimumCartSubtotal() && subtotal < fee.getMaximumCartSubtotal())
        .map(fee -> toFeeDTO(fee, subtotal))
        .collect(Collectors.toList());
  }

  @Named("feeMagnitude")
  default double feeMagnitude(Fee fee, @Context double subtotal) {
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
