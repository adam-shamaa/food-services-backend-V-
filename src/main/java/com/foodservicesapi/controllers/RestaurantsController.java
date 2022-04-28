package com.foodservicesapi.controllers;

import com.foodservices.apicodegen.RestaurantsApi;
import com.foodservices.apicodegen.model.RestaurantDetailsResponseDto;
import com.foodservices.apicodegen.model.RestaurantServiceProvidersResponseDto;
import com.foodservices.apicodegen.model.RestaurantSummarysResponseDto;
import com.foodservicesapi.mappers.RepositoryMapper;
import com.foodservicesapi.models.domain.Restaurant;
import com.foodservicesapi.services.HttpUtils;
import com.foodservicesapi.mappers.ApiMapper;
import com.foodservicesapi.models.domain.Address;
import com.foodservicesapi.models.domain.PairedRestaurantOverview;
import com.foodservicesapi.services.RestaurantsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RestaurantsController implements RestaurantsApi {

  private final RestaurantsService restaurantsService;
  private final ApiMapper apiMapper = Mappers.getMapper(ApiMapper.class);
  private final RepositoryMapper repoMapper = Mappers.getMapper(RepositoryMapper.class);
  private final HttpUtils httpUtils;

  @Override
  public ResponseEntity<RestaurantSummarysResponseDto> restaurantsGet(
      @NotNull String address, @Valid String searchQuery) {
    Address addressPojo = httpUtils.UrlEncodedStringToPojo(address, Address.class);

    List<PairedRestaurantOverview> availableRestaurantsDomain =
            restaurantsService.getRestaurants(addressPojo, searchQuery);

    if (!restaurantsService.saveRestaurantsSearchResult(
            repoMapper.toRestaurantsSearchResult(
                    null,
                    repoMapper.toRestaurantListRepository(availableRestaurantsDomain),
                    httpUtils.getClientIpAddress()))) {
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.ok(
            apiMapper.toRestaurantSummarysResponseDTO(
                    null,
                    apiMapper.toRestaurantPreviewListDTO(availableRestaurantsDomain, searchQuery)
            )
    );
  }

  @Override
  public ResponseEntity<RestaurantDetailsResponseDto> restaurantsTemporaryRestaurantUUIDGet(
      String temporaryRestaurantUUID,
      @javax.validation.constraints.NotNull String address) {

    List<Restaurant> restaurantList =
        restaurantsService.getRestaurant(
                httpUtils.UrlEncodedStringToPojo(address, Address.class), temporaryRestaurantUUID);

    return ResponseEntity.ok(
        apiMapper.toRestaurantDetailsResponseDTO(
                apiMapper.toRestaurantDetailsDTO(null, restaurantList))
    );
  }

  @Override
  public ResponseEntity<RestaurantServiceProvidersResponseDto> restaurantsTemporaryRestaurantUUIDServiceProvidersGet(String temporaryRestaurantUUID, @NotNull String address, @NotNull @Valid BigDecimal subtotalCents) {
    List<Restaurant> restaurantList =
            restaurantsService.getRestaurant(
                    httpUtils.UrlEncodedStringToPojo(address, Address.class), temporaryRestaurantUUID);

    return ResponseEntity.ok(
            apiMapper.toRestaurantServiceProvidersResponse(null, restaurantList, subtotalCents.doubleValue())
    );
  }
}
