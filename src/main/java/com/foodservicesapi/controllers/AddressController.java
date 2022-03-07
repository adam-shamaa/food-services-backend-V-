package com.foodservicesapi.controllers;

import com.foodservices.apicodegen.AddressApi;
import com.foodservices.apicodegen.model.Address;
import com.foodservicesapi.services.HttpUtils;
import com.foodservicesapi.mappers.ApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddressController implements AddressApi {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    private final HttpUtils httpUtils;

    private final ApiMapper apiMapper = Mappers.getMapper(ApiMapper.class);

    @Override
    public ResponseEntity<Void> addressPost(@Valid Address address) {
        httpServletResponse.addCookie(new Cookie("address", httpUtils.pojoToCookie(apiMapper.toAddressDomain(address))));

        return ResponseEntity.ok(null);
    }
}
