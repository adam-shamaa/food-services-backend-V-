package com.foodservicesapi.mappers;

import com.foodservicesapi.codegen.models.*;
import com.foodservicesapi.models.domain.*;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UberEatsMapper {

    double uberEatsRatingOutOf = 10;

    /*************************************** START - DOMAIN TO UberEatsDTO ******************************************************/

    // ------------------------------------ START - RestaurantListsRequest ---------------------------------------------

    RestaurantsListRequestCookieWrapperUbereats addressToRestaurantsListsRequest(Address address);

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
            @Mapping(source = "storeUuid", target = "id"),
            @Mapping(source = "title.text", target = "name"),
            @Mapping(source = "image.items", target = "imageUrl", qualifiedByName = "firstImage"),
            @Mapping(source = "rating.text", target = "rating", qualifiedByName = "uberEatsRatingToDomainRatingScale"),
            @Mapping(target = "serviceProvider", constant = "UBEREATS")
    })
    RestaurantOverview restaurantOverviewToRestaurantOverviewDomain(RestaurantsListResponseStoreUbereats restaurant);
    List<RestaurantOverview> restaurantsOverviewsToRestaurantOverviewsDomain(List<RestaurantsListResponseStoreUbereats> restaurants);

    // ------------------------------------ END - TO RestaurantOverviewDomain -------------------------------------------

    // ------------------------------------ START - RestaurantDetailDomain ---------------------------------------

    @Mappings({
            @Mapping(source = "data.uuid", target = "id"),
            @Mapping(source = "data.title", target = "restaurantName"),
            @Mapping(source = "data.location.address", target = "formattedAddress"),
            @Mapping(source = "data.breadcrumbs.value", target = "redirectUrl", qualifiedByName = "lastBreadCrumbItem"),
            @Mapping(source = "data.rating.ratingValue", target = "rating"),
            @Mapping(source = "data.heroImageUrls", target = "imageUrl", qualifiedByName = "lastImage"),
            @Mapping(source = "data.fareInfo", target = "fees", qualifiedByName = "toFeeListDomain"),
            @Mapping(source = "data.catalogSectionsMap", target = "menu", qualifiedByName = "toMenuCategoriesListDomain"),
            @Mapping(target = "serviceProviderName", constant = "UBEREATS"),
    })
    Restaurant toRestaurantDomain(RestaurantDetailsResponseWrapperUbereats restaurantWrapper);

    @Named("toMenuCategoriesListDomain")
    default List<MenuCategory> toMenuCategoriesListDomain(RestaurantDetailsResponseCatalogSectionMapUbereats menu) {
        return menu
                .getAdditionalProperties()
                .values()
                .stream()
                .flatMap(catalog ->
                        catalog
                                .stream()
                                .map(this::toMenuCategoryDomain)
                )
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
            @Mapping(target = "percent", constant = "false"),
            @Mapping(source = "serviceFeeCents", target = "magnitude"),
            @Mapping(target = "magnitudeUnits", constant = "CENTS"),
            @Mapping(target = "minimumCartSubtotal", constant = "0L"),
            @Mapping(target = "maximumCartSubtotal", constant = "9999999L"),
            @Mapping(target = "currency", constant = "CAD"),
            @Mapping(target = "minimumFeeMagnitude", constant = "0L"),
            @Mapping(target = "maximumFeeMagnitude", constant = "9999999L"),
    })
    Fee toFeeDomain(RestaurantDetailsResponseFareInfoUbereats fee);

    @Named("toFeeListDomain")
    default List<Fee> toFeeListDomain(RestaurantDetailsResponseFareInfoUbereats fee) {
        return Arrays.asList(toFeeDomain(fee));
    }

    @Named("lastImage")
    default String lastImage(List<RestaurantDetailsResponseHeroImageUrlUbereats> items) {
        return items != null && !items.isEmpty() ? items.get(items.size()-1).getUrl() : null;
    }

    @Named("lastBreadCrumbItem")
    default String lastBreadCrumbItem(List<RestaurantDetailsResponseBreadcrumbItemUbereats> items) {
        return items != null && !items.isEmpty() ? "https://www.ubereats.com" +items.get(items.size()-1).getHref() : null;
    }

    @Named("firstImage")
    default String firstImage(List<RestaurantsListResponseImagePropertyUbereats> images) {
        return images != null && !images.isEmpty() ? images.get(0).getUrl() : null;
    }

    // ------------------------------------ END - RestaurantDetailDomain --------------------------------------------

    @Named("uberEatsRatingToDomainRatingScale")
    static double convertRatingToScaleOfTen(double skipScore) {
        return Math.round((skipScore * (10 / uberEatsRatingOutOf) * 100)) / 100;
    }

}
