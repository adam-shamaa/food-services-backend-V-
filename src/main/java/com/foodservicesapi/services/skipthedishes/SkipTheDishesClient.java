package com.foodservicesapi.services.skipthedishes;

import com.foodservicesapi.codegen.models.RestaurantDetailsRequestSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantDetailsResponseWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListRequestWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListResponseWrapperSkipTheDishes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SkipTheDishesClient {

  private final WebClient webClient;

  public SkipTheDishesClient() {
    this.webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                    .build())
            .build();
  }

  public RestaurantsListResponseWrapperSkipTheDishes fetchRestaurantsLists(
      RestaurantsListRequestWrapperSkipTheDishes requestBody) {
    RestaurantsListResponseWrapperSkipTheDishes response = this.webClient
            .post()
            .uri("https://api.skipthedishes.com/customer/v1/graphql")
            .accept(MediaType.APPLICATION_JSON)
            .header("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc")
            .body(Mono.just(requestBody), RestaurantsListRequestWrapperSkipTheDishes.class)
            .retrieve()
            .bodyToMono(RestaurantsListResponseWrapperSkipTheDishes.class)
            .block();
    return response;
  }

  public RestaurantDetailsResponseWrapperSkipTheDishes fetchRestaurantDetails(RestaurantDetailsRequestSkipTheDishes request) {
    RestaurantDetailsResponseWrapperSkipTheDishes response =
        this.webClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                            .scheme("https")
                            .host("api-skipthedishes.skipthedishes.com")
                            .path("/v1/restaurants/clean-url/")
                            .path(request.getId())
                            .queryParam("fullMenu", false)
                            .queryParam("language", "en")
                            .queryParam("lat", request.getLatitude())
                            .queryParam("long", request.getLongitude())
                            .queryParam("order_type", "DELIVERY")
                            .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .header("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc")
            .retrieve()
            .bodyToMono(RestaurantDetailsResponseWrapperSkipTheDishes.class)
            .block();
    return response;
  }
}
