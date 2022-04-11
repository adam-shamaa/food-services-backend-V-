package com.foodservicesapi.mappers;

import com.foodservicesapi.codegen.models.*;
import com.foodservicesapi.models.domain.*;
import com.foodservicesapi.models.domain.enums.CurrencyUnitsEnum;
import com.foodservicesapi.models.domain.enums.FeeTypeEnum;
import org.mapstruct.*;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface UberEatsMapper {

  double uberEatsRatingOutOf = 5;

  /*************************************** START - DOMAIN TO UberEatsDTO ******************************************************/

  // ------------------------------------ START - RestaurantListsRequest ---------------------------------------------

  RestaurantsListRequestCookieWrapperUbereats toRestaurantsListsRequestCookie(Address address);

  @Mapping(source = "searchQuery", target = "userQuery")
  @Mapping(target = "pageInfo.offset", constant = "1")
  @Mapping(target = "pageInfo.pageSize", constant = "5000")
  RestaurantsListRequestBodyWrapperUbereats toRestaurantsListsRequestBody(String searchQuery);

  // ------------------------------------- END - RestaurantListsRequest ----------------------------------------------

  // ------------------------------------ START - RestaurantDetailsRequest -------------------------------------------

  @Mappings({
    @Mapping(source = "address.latitude", target = "latitude"),
    @Mapping(source = "address.longitude", target = "longitude"),
  })
  RestaurantDetailsRequestCookieUbereats toRestaurantDetailsRequestCookie(Address address);

  @Mappings({
    @Mapping(source = "id", target = "storeUuid"),
  })
  RestaurantDetailsRequestBodyUbereats toRestaurantDetailsRequestBody(String id);

  // ------------------------------------ END - RestaurantDetailsRequest ---------------------------------------------

  /*************************************** END - DOMAIN TO UberEatsDTO ******************************************************/

  // ------------------------------------ START - TO RestaurantOverviewDomain -------------------------------------------

  @Mappings({
    @Mapping(source = "restaurant.storeUuid", target = "id"),
    @Mapping(source = "restaurant.title.text", target = "name"),
    @Mapping(
        source = "restaurant.image.items",
        target = "imageUrl",
        qualifiedByName = "firstImage"),
    @Mapping(
        source = "restaurant.rating.text",
        target = "rating",
        qualifiedByName = "uberEatsRatingToDomainRatingScale"),
    @Mapping(
        source = "restaurant",
        target = "minEstimatedDeliveryTimeMinutes",
        qualifiedByName = "firstDeliveryTimeOverview"),
    @Mapping(
        source = "restaurant",
        target = "maxEstimatedDeliveryTimeMinutes",
        qualifiedByName = "secondDeliveryTimeOverview"),
    @Mapping(target = "serviceProvider", constant = "UBEREATS")
  })
  RestaurantOverview restaurantOverviewToRestaurantOverviewDomain(
      Integer dummyIntegerIgnore, RestaurantsListResponseStoreUbereats restaurant);

  default List<RestaurantOverview> restaurantsOverviewsToRestaurantOverviewsDomain(
      List<RestaurantsListResponseStoreUbereats> restaurants) {
    return restaurants.stream()
        .map(restaurant -> this.restaurantOverviewToRestaurantOverviewDomain(null, restaurant))
        .collect(Collectors.toList());
  }

  @Named("firstDeliveryTimeOverview")
  default Integer firstDeliveryTimeOverview(RestaurantsListResponseStoreUbereats restaurant) {
    List<RestaurantsListResponseStoreMetaUbereats> metaUbereatsList = new ArrayList<>();
    if (restaurant.getMeta() != null) metaUbereatsList.addAll(restaurant.getMeta());
    if (restaurant.getMeta2() != null) metaUbereatsList.addAll(restaurant.getMeta2());
    Optional<RestaurantsListResponseStoreMetaUbereats> text =
        metaUbereatsList.stream()
            .filter(listItem -> listItem.getText() != null)
            .filter(listItem -> listItem.getText().contains("min"))
            .findFirst();
    return text != null && text.isPresent()
        ? Integer.parseInt(text.get().getText().split(" ")[0].split("–")[0])
        : -1;
  }

  @Named("secondDeliveryTimeOverview")
  default Integer secondDeliveryTimeOverview(RestaurantsListResponseStoreUbereats restaurant) {
    List<RestaurantsListResponseStoreMetaUbereats> metaUbereatsList = new ArrayList<>();
    if (restaurant.getMeta() != null) metaUbereatsList.addAll(restaurant.getMeta());
    if (restaurant.getMeta2() != null) metaUbereatsList.addAll(restaurant.getMeta2());
    Optional<RestaurantsListResponseStoreMetaUbereats> text =
            metaUbereatsList.stream()
                    .filter(listItem -> listItem.getText() != null)
                    .filter(listItem -> listItem.getText().contains("min"))
                    .findFirst();
    return text != null && text.isPresent()
        ? Integer.parseInt(text.get().getText().split(" ")[0].split("–")[1])
        : -1;
  }

  // ------------------------------------ END - TO RestaurantOverviewDomain -------------------------------------------

  // ------------------------------------ START - RestaurantDetailDomain ---------------------------------------

  @Mappings({
    @Mapping(source = "data.uuid", target = "id"),
    @Mapping(source = "data.title", target = "restaurantName"),
    @Mapping(source = "data.location.address", target = "formattedAddress"),
    @Mapping(
        source = "data.breadcrumbs.value",
        target = "redirectUrl",
        qualifiedByName = "lastBreadCrumbItem"),
    @Mapping(source = "data.rating.ratingValue", target = "rating"),
    @Mapping(source = "data.heroImageUrls", target = "imageUrl", qualifiedByName = "lastImage"),
    @Mapping(source = "data.fareInfo", target = "fees", qualifiedByName = "toFeeListDomain"),
    @Mapping(
        source = "data.catalogSectionsMap",
        target = "menu",
        qualifiedByName = "toMenuCategoriesListDomain"),
    @Mapping(target = "serviceProviderName", constant = "UBEREATS"),
    @Mapping(
        source = "data.etaRange",
        target = "minEstimatedDeliveryTime",
        qualifiedByName = "firstDeliveryTimeDetailed"),
    @Mapping(
        source = "data.etaRange",
        target = "maxEstimatedDeliveryTime",
        qualifiedByName = "secondDeliveryTimeDetailed"),
  })
  Restaurant toRestaurantDomain(RestaurantDetailsResponseWrapperUbereats restaurantWrapper);

  @Named("toMenuCategoriesListDomain")
  default List<MenuCategory> toMenuCategoriesListDomain(
      RestaurantDetailsResponseCatalogSectionMapUbereats menu) {
    return menu.getAdditionalProperties().values().stream()
        .flatMap(catalog -> catalog.stream().map(this::toMenuCategoryDomain))
        .collect(Collectors.toList());
  }

  @Mappings({
    @Mapping(source = "payload.standardItemsPayload.title.text", target = "name"),
    @Mapping(source = "payload.standardItemsPayload.catalogItems", target = "items"),
  })
  MenuCategory toMenuCategoryDomain(RestaurantDetailsResponseCatalogUbereats catalogUbereats);

  @Mappings({
    @Mapping(source = "title", target = "name"),
    @Mapping(source = "itemDescription", target = "description"),
    @Mapping(source = "price", target = "price"),
    @Mapping(target = "currency", constant = "CAD"),
    @Mapping(target = "priceUnits", constant = "CENTS"),
  })
  MenuItem toMenuItemDomain(RestaurantDetailsResponseCatalogItemUbereats menuItemUbereats);

  @Mappings({
    @Mapping(target = "type", constant = "DELIVERY"),
    @Mapping(target = "isPercent", constant = "false"),
    @Mapping(source = "serviceFeeCents", target = "magnitude"),
    @Mapping(target = "magnitudeUnits", constant = "CENTS"),
    @Mapping(target = "minimumCartSubtotal", constant = "0L"),
    @Mapping(target = "maximumCartSubtotal", constant = "9999999L"),
    @Mapping(target = "currency", constant = "CAD"),
    @Mapping(target = "minimumFeeMagnitude", constant = "0L"),
    @Mapping(target = "maximumFeeMagnitude", constant = "9999999L"),
  })
  Fee toFeeDomain(RestaurantDetailsResponseFareInfoUbereats fee);

  @Named("firstDeliveryTimeDetailed")
  default int firstDeliveryTimeDetailed(RestaurantDetailsResponseEtaRangeUbereats etaRange) {
    return etaRange != null ? Integer.parseInt(etaRange.getText().split(" ")[0].split("–")[0]) : -1;
  }

  @Named("secondDeliveryTimeDetailed")
  default int secondDeliveryTimeDetailed(RestaurantDetailsResponseEtaRangeUbereats etaRange) {
    return etaRange != null ? Integer.parseInt(etaRange.getText().split(" ")[0].split("–")[1]) : -1;
  }

  @Named("toFeeListDomain")
  default List<Fee> toFeeListDomain(RestaurantDetailsResponseFareInfoUbereats fee) {
    return Arrays.asList(
        toFeeDomain(fee),
        Fee.builder()
            .type(FeeTypeEnum.SERVICE)
            .isPercent(true)
            .magnitude(0.1)
            .minimumFeeMagnitude(200)
            .maximumFeeMagnitude(400)
            .minimumCartSubtotal(0)
            .maximumCartSubtotal(Integer.MAX_VALUE)
            .magnitudeUnits(CurrencyUnitsEnum.CENTS)
            .build());
  }

  @Named("lastImage")
  default String lastImage(List<RestaurantDetailsResponseHeroImageUrlUbereats> items) {
    return items != null && !items.isEmpty() ? items.get(items.size() - 1).getUrl() : null;
  }

  @Named("lastBreadCrumbItem")
  default String lastBreadCrumbItem(List<RestaurantDetailsResponseBreadcrumbItemUbereats> items) {
    return items != null && !items.isEmpty()
        ? "https://www.ubereats.com" + items.get(items.size() - 1).getHref()
        : null;
  }

  @Named("firstImage")
  default String firstImage(List<RestaurantsListResponseImagePropertyUbereats> images) {
    return images != null && !images.isEmpty() ? images.get(0).getUrl() : null;
  }

  // ------------------------------------ END - RestaurantDetailDomain --------------------------------------------

  @Named("uberEatsRatingToDomainRatingScale")
  static double convertRatingToScaleOfTen(String rating) {
    return rating != null
        ? Math.round((Double.parseDouble(rating) * (10.0 / uberEatsRatingOutOf) * 100.0)) / 100.0
        : 0;
  }
}
