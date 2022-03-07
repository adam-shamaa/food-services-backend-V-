package com.foodservicesapi.services.ubereats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodservicesapi.codegen.models.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

@Slf4j
@Component
public class UberEatsClient {
    private final WebClient webClient;

    public UberEatsClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://www.ubereats.com/api/")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @SneakyThrows
    public RestaurantsListResponseWrapperUbereats fetchRestaurantsLists(
            RestaurantsListRequestCookieWrapperUbereats requestCookie) {
        RestaurantsListResponseWrapperUbereats response = this.webClient
                .post()
                .uri("getFeedV1")
                .accept(MediaType.APPLICATION_JSON)
                .header("x-csrf-token", "x")
                .header("Cookie", String.format("uev2.loc=%s", URLEncoder.encode(new ObjectMapper().writeValueAsString(requestCookie), "UTF-8")))
                .retrieve()
                .bodyToMono(RestaurantsListResponseWrapperUbereats.class)
                .block();
        return response;
    }

    @SneakyThrows
    public RestaurantDetailsResponseWrapperUbereats fetchRestaurantDetails(RestaurantDetailsRequestCookieUbereats requestCookie, RestaurantDetailsRequestBodyUbereats requestBody) {
        RestaurantDetailsResponseWrapperUbereats response = this.webClient
                .post()
                .uri("https://www.ubereats.com/api/getStoreV1?localeCode=ca")
                .accept(MediaType.APPLICATION_JSON)
                .header("x-csrf-token", "x")
                .header("Cookie", String.format("uev2.loc=%s", URLEncoder.encode(new ObjectMapper().writeValueAsString(requestCookie), "UTF-8")))
                .body(Mono.just(requestBody), RestaurantDetailsRequestBodyUbereats.class)
                .retrieve()
                .bodyToMono(RestaurantDetailsResponseWrapperUbereats.class)
                .block();
        return response;
    }
}
