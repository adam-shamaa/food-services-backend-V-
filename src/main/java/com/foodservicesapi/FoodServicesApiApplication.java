package com.foodservicesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class FoodServicesApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(FoodServicesApiApplication.class, args);
  }
}
