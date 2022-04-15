package com.foodservicesapi.services.skipthedishes;

import com.foodservicesapi.codegen.models.RestaurantDetailsRequestSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantDetailsResponseWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListRequestWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListResponseWrapperSkipTheDishes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SkipTheDishesClient {

  private final RestTemplate webClient;

  public SkipTheDishesClient() {
    this.webClient = new RestTemplate();
  }

  public RestaurantsListResponseWrapperSkipTheDishes fetchRestaurantsLists(
      RestaurantsListRequestWrapperSkipTheDishes requestBody, String searchQuery) {
    return this.webClient
            .exchange("https://api.skipthedishes.com/customer/v1/graphql",
                    HttpMethod.POST,
                    new HttpEntity(requestBody, new HttpHeaders(){{
                      set("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc");
                      set("Parameters", "isCuisineSearch=false&isSorted=false&search=" + searchQuery);
                    }}),
                    RestaurantsListResponseWrapperSkipTheDishes.class
                  )
            .getBody();
  }

  @SneakyThrows
  public RestaurantDetailsResponseWrapperSkipTheDishes fetchRestaurantDetails(
      RestaurantDetailsRequestSkipTheDishes request) {
    return this.webClient
            .exchange(
                    new URIBuilder()
                      .setScheme("https")
                      .setHost("api-skipthedishes.skipthedishes.com")
                      .setPath("/v1/restaurants/clean-url/" + request.getId())
                      .addParameter("fullMenu", "false")
                      .addParameter("language", "en")
                      .addParameter("lat", request.getLatitude().toString())
                      .addParameter("long", request.getLongitude().toString())
                      .addParameter("order_type", "DELIVERY")
                      .build(),
                    HttpMethod.GET,
                    new HttpEntity(new HttpHeaders(){{
                      set("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc");
                    }}),
                    RestaurantDetailsResponseWrapperSkipTheDishes.class
            ).getBody();
  }
}
