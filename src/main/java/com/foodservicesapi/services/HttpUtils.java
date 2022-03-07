package com.foodservicesapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;

@Service
public class HttpUtils {

    @SneakyThrows
    public String pojoToCookie(Object object) {
        return URLEncoder.encode(new ObjectMapper().writeValueAsString(object), "UTF-8");
    }

    @SneakyThrows
    public <T> T cookieToPojo(String cookie, Class<T> obj) {
        return new ObjectMapper().readValue(URLDecoder.decode(cookie, "UTF-8"), obj);
    }
}
