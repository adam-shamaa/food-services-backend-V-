package com.foodservicesapi.controllers;

import com.foodservices.apicodegen.PingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PingController implements PingApi {
    @Override
    public ResponseEntity<String> pingGet() {
        return ResponseEntity.ok("pong");
    }
}
