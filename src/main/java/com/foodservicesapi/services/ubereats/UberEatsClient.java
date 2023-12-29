package com.foodservicesapi.services.ubereats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodservicesapi.codegen.models.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@Slf4j
@Component
public class UberEatsClient {
  private final RestTemplate webClient;

  public UberEatsClient() {
    this.webClient = new RestTemplate();
  }

  @SneakyThrows
  public RestaurantsListResponseWrapperUbereats fetchRestaurantsLists(
          RestaurantsListRequestCookieWrapperUbereats requestCookie,
          RestaurantsListRequestBodyWrapperUbereats requestBody) {
      RestaurantsListResponseWrapperUbereats res = this.webClient
            .exchange(
                    "https://www.ubereats.com/api/getFeedV1",
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, new HttpHeaders() {{
                      set("x-csrf-token", "x");
                      set("Cookie", String.format("uev2.loc=%s", URLEncoder.encode(new ObjectMapper().writeValueAsString(requestCookie), "UTF-8")));
                    }}),
                    RestaurantsListResponseWrapperUbereats.class
            ).getBody();
      return res;
  }

  @SneakyThrows
  public RestaurantDetailsResponseWrapperUbereats fetchRestaurantDetails(
          RestaurantDetailsRequestCookieUbereats requestCookie,
          RestaurantDetailsRequestBodyUbereats requestBody) {
    return this.webClient
            .exchange("https://www.ubereats.com/api/getStoreV1?localeCode=ca",
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, new HttpHeaders() {{
                      set("x-csrf-token", "x");
                      set("Cookie",
                              String.format(
                                      "uev2.loc=%s",
                                      URLEncoder.encode(new ObjectMapper().writeValueAsString(requestCookie), "UTF-8")));
                    }}),
                    RestaurantDetailsResponseWrapperUbereats.class
                    ).getBody();
  }
}
