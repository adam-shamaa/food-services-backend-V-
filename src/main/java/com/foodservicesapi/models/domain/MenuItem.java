package com.foodservicesapi.models.domain;

import com.foodservicesapi.models.domain.enums.CurrencyEnum;
import com.foodservicesapi.models.domain.enums.CurrencyUnitsEnum;
import lombok.*;

@Data
@RequiredArgsConstructor
public class MenuItem {
  private String name;
  private String description;
  private double price;
  private CurrencyEnum currency;
  private CurrencyUnitsEnum priceUnits;
}
