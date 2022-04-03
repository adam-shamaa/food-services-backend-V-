package com.foodservicesapi.mappers;

import com.foodservicesapi.codegen.models.*;
import com.foodservicesapi.models.domain.*;
import com.foodservicesapi.models.domain.enums.FeeTypeEnum;
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
    @Mapping(source = "restaurant.name", target = "restaurantName"),
    @Mapping(source = "restaurant.menu.menuGroups", target = "menu"),
    @Mapping(source = "restaurant.location.address", target = "formattedAddress"),
    @Mapping(
        source = "restaurant.cleanUrl",
        target = "redirectUrl",
        qualifiedByName = "skipTheDishesRedirectUrl"),
    @Mapping(
        source = "restaurant.skipScore",
        target = "rating",
        qualifiedByName = "skipScoreToDomainRatingScale"),
    @Mapping(source = "restaurant.imageUrls.menuImageLargeUrl", target = "imageUrl"),
    @Mapping(source = "restaurant", target = "fees", qualifiedByName = "skipTheDishesFees"),
    @Mapping(source = "restaurant.minEstimatedTime", target = "minEstimatedDeliveryTime"),
    @Mapping(source = "restaurant.maxEstimatedTime", target = "maxEstimatedDeliveryTime"),
    @Mapping(target = "serviceProviderName", constant = "SKIPTHEDISHES"),
  })
  Restaurant toRestaurantDomain(
      Integer integer, RestaurantDetailsResponseWrapperSkipTheDishes restaurant);

  @Mappings({
    @Mapping(source="feeType", target = "type"),
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
      RestaurantDetailsResponseFeeSkipTheDishes feeSkipTheDishes, double maximumCartSubtotal, FeeTypeEnum feeType);

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
  default List<Fee> skipTheDishesDeliveryFees(
          RestaurantDetailsResponseWrapperSkipTheDishes skipTheDishesRestaurant
  ) {
    List<Fee> feeArrayList = new ArrayList<>();

    if (skipTheDishesRestaurant.getFees() != null)feeArrayList.addAll(skipTheDishesDeliveryFees(skipTheDishesRestaurant.getFees()));
    if (skipTheDishesRestaurant.getRestaurantCustomTaxes() != null) feeArrayList.addAll(skipTheDishesServiceFees(skipTheDishesRestaurant.getRestaurantCustomTaxes()));

    return feeArrayList;
  }

  @Named("skipTheDishesDeliveryFees")
  default List<Fee> skipTheDishesDeliveryFees(
      List<RestaurantDetailsResponseFeeSkipTheDishes> feesSkipTheDishes) {
    List<Fee> feeArrayList = new ArrayList<>();

    feesSkipTheDishes.sort(
        (o1, o2) -> Double.compare(o2.getOrderMinimumCents(), o1.getOrderMinimumCents()));
    for (int i = 0; i < feesSkipTheDishes.size(); i++) {
      feeArrayList.add(
          toFeeDomain(
              feesSkipTheDishes.get(i),
              i > 0 ? feesSkipTheDishes.get(i - 1).getOrderMinimumCents() : Integer.MAX_VALUE,
              FeeTypeEnum.DELIVERY
              ));
    }

    return feeArrayList;
  }

  @Named("skipTheDishesServiceFees")
  default List<Fee> skipTheDishesServiceFees(
          List<RestaurantDetailsResponseCustomTaxSkipTheDishes> feesSkipTheDishes) {
    List<Fee> feeArrayList = new ArrayList<>();

    for (RestaurantDetailsResponseCustomTaxSkipTheDishes fee : feesSkipTheDishes) {
      if (fee.getPriceCategory().equals(RestaurantDetailsResponseCustomTaxSkipTheDishes.PriceCategory.CUSTOMER_SERVICE_FEE)){
        feeArrayList.add(toFeeDomain(
                new RestaurantDetailsResponseFeeSkipTheDishes()
                        .withFeeCents(fee.getTaxRate())
                        .withOrderMinimumCents(0.00),
                Integer.MAX_VALUE,
                FeeTypeEnum.SERVICE
        ));
      }
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
