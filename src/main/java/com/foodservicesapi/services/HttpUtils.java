package com.foodservicesapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Service
public class HttpUtils {

    @SneakyThrows
    public String pojoToUrlEncodedString(Object object) {
        return URLEncoder.encode(new ObjectMapper().writeValueAsString(object), "UTF-8");
    }

    @SneakyThrows
    public <T> T UrlEncodedStringToPojo(String cookie, Class<T> obj) {
        return new ObjectMapper().readValue(URLDecoder.decode(cookie, "UTF-8"), obj);
    }

    public String getClientIpAddress() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getRemoteAddr();
    }
}
