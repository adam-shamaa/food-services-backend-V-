package com.foodservicesapi.services.skipthedishes;

import com.foodservicesapi.codegen.models.RestaurantDetailsRequestSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantDetailsResponseWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListRequestWrapperSkipTheDishes;
import com.foodservicesapi.codegen.models.RestaurantsListResponseWrapperSkipTheDishes;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@Component
public class SkipTheDishesClient {

  private final WebClient webClient;

  public SkipTheDishesClient() {
    this.webClient =
        WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs(
                        configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                    .build())
            .build();
  }

  public RestaurantsListResponseWrapperSkipTheDishes fetchRestaurantsLists(
      RestaurantsListRequestWrapperSkipTheDishes requestBody, String searchQuery) {
    return this.webClient
        .post()
        .uri("https://api.skipthedishes.com/customer/v1/graphql")
        .accept(MediaType.APPLICATION_JSON)
        .header("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc")
        .header("Parameters", "isCuisineSearch=false&isSorted=false&search=" + searchQuery)
        .body(Mono.just(requestBody), RestaurantsListRequestWrapperSkipTheDishes.class)
        .retrieve()
        .bodyToMono(RestaurantsListResponseWrapperSkipTheDishes.class)
        .block();
  }

  public RestaurantDetailsResponseWrapperSkipTheDishes fetchRestaurantDetails(
      RestaurantDetailsRequestSkipTheDishes request) {
    return this.webClient
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
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .header("App-Token", "d7033722-4d2e-4263-9d67-d83854deb0fc")
        .retrieve()
        .bodyToMono(RestaurantDetailsResponseWrapperSkipTheDishes.class)
        .block();
  }
}
