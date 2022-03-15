package com.foodservicesapi.mappers;

import com.foodservicesapi.codegen.models.*;
import com.foodservicesapi.models.domain.*;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SkipTheDishesMapper {

  double skipScoreOutOf = 100;

  /*************************************** START - DOMAIN TO SKIPTHEDISHESDTO ******************************************************/

  @Mappings({
    @Mapping(target = "operationName", constant = "QueryRestaurantsCuisinesList"),
    @Mapping(target = "extensions.persistedQuery.version", constant = "1"),
    @Mapping(
        target = "extensions.persistedQuery.sha256Hash",
        constant = "e4650bd94c9d17af7fbc24dfc1475a6fb6f496a49d9e967dc40ef4e40cf747bc"),
    @Mapping(target = "variables.isDelivery", constant = "true"),
    @Mapping(target = "variables.language", constant = "en"),
    @Mapping(source = "address.city", target = "variables.city"),
    @Mapping(source = "address.province", target = "variables.province"),
    @Mapping(source = "address.latitude", target = "variables.latitude"),
    @Mapping(source = "address.longitude", target = "variables.longitude")
  })
  RestaurantsListRequestWrapperSkipTheDishes toRestaurantsListRequest(Address address);

  @Mappings({
    @Mapping(source = "restaurantId", target = "id"),
    @Mapping(source = "address.latitude", target = "latitude"),
    @Mapping(source = "address.longitude", target = "longitude"),
    @Mapping(target = "language", constant = "EN"),
    @Mapping(target = "orderType", constant = "DELIVERY"),
  })
  RestaurantDetailsRequestSkipTheDishes toRestaurantDetailsRequest(
      Address address, String restaurantId);

  /*************************************** END - DOMAIN TO SKIPTHEDISHESDTO ******************************************************/

  /*************************************** START - SKIPTHEDISHESDTO TO DOMAIN ******************************************************/

  // ------------------------------------ START - TO RestaurantOverview ---------------------------------------------

  List<RestaurantOverview> toRestaurantOverviewDomainList(
      List<RestaurantsListResponseOpenRestaurantsSkipTheDishes> restaurants);

  @Mappings({
    @Mapping(source = "cleanUrl", target = "id"),
    @Mapping(source = "name", target = "name"),
    @Mapping(
        source = "skipScore",
        target = "rating",
        qualifiedByName = "skipScoreToDomainRatingScale"),
    @Mapping(source = "imageUrls.menuSmallUrl", target = "imageUrl"),
    @Mapping(source = "minEstimatedTime", target = "minEstimatedDeliveryTimeMinutes"),
    @Mapping(source = "maxEstimatedTime", target = "maxEstimatedDeliveryTimeMinutes"),
    @Mapping(target = "serviceProvider", constant = "SKIPTHEDISHES")
  })
  RestaurantOverview toRestaurantOverviewDomain(
      RestaurantsListResponseOpenRestaurantsSkipTheDishes restaurant);

  // ------------------------------------ END - TO RestaurantOverview ---------------------------------------------

  // ------------------------------------ START - TO RestaurantDomain ---------------------------------------------

  @Mappings({
    @Mapping(source = "name", target = "restaurantName"),
    @Mapping(source = "menu.menuGroups", target = "menu"),
    @Mapping(source = "location.address", target = "formattedAddress"),
    @Mapping(
        source = "cleanUrl",
        target = "redirectUrl",
        qualifiedByName = "skipTheDishesRedirectUrl"),
    @Mapping(
        source = "skipScore",
        target = "rating",
        qualifiedByName = "skipScoreToDomainRatingScale"),
    @Mapping(source = "imageUrls.menuImageLargeUrl", target = "imageUrl"),
    @Mapping(source = "fees", target = "fees", qualifiedByName = "skipTheDishesFees"),
    @Mapping(source = "minEstimatedTime", target = "minEstimatedDeliveryTime"),
    @Mapping(source = "maxEstimatedTime", target = "maxEstimatedDeliveryTime"),
    @Mapping(target = "serviceProviderName", constant = "SKIPTHEDISHES"),
  })
  Restaurant toRestaurantDomain(
      RestaurantDetailsResponseWrapperSkipTheDishes skipTheDishesRestaurant);

  @Mappings({
    @Mapping(target = "type", constant = "DELIVERY"),
    @Mapping(target = "isPercent", constant = "false"),
    @Mapping(source = "feeSkipTheDishes.feeCents", target = "magnitude"),
    @Mapping(source = "feeSkipTheDishes.orderMinimumCents", target = "minimumCartSubtotal"),
    @Mapping(source = "maximumCartSubtotal", target = "maximumCartSubtotal"),
    @Mapping(target = "currency", constant = "CAD"),
    @Mapping(target = "minimumFeeMagnitude", constant = "0L"),
    @Mapping(target = "maximumFeeMagnitude", constant = "9999L"),
    @Mapping(target = "magnitudeUnits", constant = "CENTS"),
  })
  Fee toFeeDomain(
      RestaurantDetailsResponseFeeSkipTheDishes feeSkipTheDishes, double maximumCartSubtotal);

  @Mappings({@Mapping(source = "menuItems", target = "items")})
  MenuCategory toMenuCategoryDomain(
      RestaurantDetailsResponseMenuGroupSkipTheDishes menuGroupSkipTheDishes);

  @Mappings({
    @Mapping(source = "centsPrice", target = "price"),
    @Mapping(target = "priceUnits", constant = "CENTS"),
    @Mapping(target = "currency", constant = "CAD"),
  })
  MenuItem toMenuItemDomain(RestaurantDetailsResponseMenuItemSkipTheDishes menuItemSkipTheDishes);

  @Named("skipTheDishesRedirectUrl")
  static String skipTheDishesRedirectUrl(String cleanUrlPrefix) {
    return String.format("https://www.skipthedishes.com/%s", cleanUrlPrefix);
  }

  @Named("skipTheDishesFees")
  default List<Fee> skipTheDishesFees(
      List<RestaurantDetailsResponseFeeSkipTheDishes> feesSkipTheDishes) {
    List<Fee> feeArrayList = new ArrayList<>();

    feesSkipTheDishes.sort(
        (o1, o2) -> Double.compare(o2.getOrderMinimumCents(), o1.getOrderMinimumCents()));
    for (int i = 0; i < feesSkipTheDishes.size(); i++) {
      feeArrayList.add(
          toFeeDomain(
              feesSkipTheDishes.get(i),
              i > 0 ? feesSkipTheDishes.get(i - 1).getOrderMinimumCents() : Integer.MAX_VALUE));
    }

    return feeArrayList;
  }

  // ------------------------------------ END - TO RestaurantDomain ---------------------------------------------

  @Named("skipScoreToDomainRatingScale")
  static double skipScoreToDomainRatingScale(double skipScore) {
    return Math.round((skipScore * (10.0 / skipScoreOutOf) * 100.0)) / 100.0;
  }

  /*************************************** END - SKIPTHEDISHESDTO TO DOMAIN  ******************************************************/
}
