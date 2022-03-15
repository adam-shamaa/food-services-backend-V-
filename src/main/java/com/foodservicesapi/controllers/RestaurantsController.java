package com.foodservicesapi.controllers;

import com.foodservices.apicodegen.RestaurantsApi;
import com.foodservices.apicodegen.model.DetailedRestaurantResponse;
import com.foodservices.apicodegen.model.SummaryRestaurantResponse;
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
  private final HttpUtils httpUtils;

  @Override
  public ResponseEntity<List<SummaryRestaurantResponse>> restaurantsGet(
      @NotNull String address, @Valid String searchQuery) {
    Address addressPojo = httpUtils.cookieToPojo(address, Address.class);
    List<PairedRestaurantOverview> availableRestaurantsDomain =
        restaurantsService.getRestaurants(addressPojo, searchQuery);
    return ResponseEntity.ok(apiMapper.toRestaurantPreviewListDTO(availableRestaurantsDomain));
  }

  @Override
  public ResponseEntity<DetailedRestaurantResponse> restaurantsTemporaryRestaurantUUIDGet(
      String temporaryRestaurantUUID,
      @javax.validation.constraints.NotNull String address,
      BigDecimal subtotal) {
    Address addressPojo = httpUtils.cookieToPojo(address, Address.class);
    List<Restaurant> restaurantList =
        restaurantsService.getRestaurant(addressPojo, temporaryRestaurantUUID);
    return ResponseEntity.ok(
        apiMapper.toRestaurantAggregateDTO(null, restaurantList, subtotal.doubleValue()));
  }
}
