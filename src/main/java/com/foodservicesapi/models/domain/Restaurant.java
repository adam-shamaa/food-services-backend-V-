package com.foodservicesapi.models.domain;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class Restaurant extends RestaurantMetadata {
  private List<Fee> fees;
  private List<MenuCategory> menu;
}
