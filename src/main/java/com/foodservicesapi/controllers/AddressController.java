package com.foodservicesapi.controllers;

import com.foodservices.apicodegen.AddressApi;
import com.foodservices.apicodegen.model.AddressRequestDto;
import com.foodservicesapi.services.HttpUtils;
import com.foodservicesapi.mappers.ApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddressController implements AddressApi {

  private final HttpServletResponse httpServletResponse;
  private final HttpUtils httpUtils;
  private final ApiMapper apiMapper = Mappers.getMapper(ApiMapper.class);

  @Override
  public ResponseEntity<Void> addressPost(AddressRequestDto address) {
    Cookie cookie = new Cookie("address", httpUtils.pojoToUrlEncodedString(apiMapper.toAddressDomain(address)));
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");

    httpServletResponse.addCookie(cookie);
    return ResponseEntity.ok(null);
  }
}
