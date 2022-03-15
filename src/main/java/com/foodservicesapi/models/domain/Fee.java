package com.foodservicesapi.models.domain;

import com.foodservicesapi.models.domain.enums.CurrencyEnum;
import com.foodservicesapi.models.domain.enums.CurrencyUnitsEnum;
import com.foodservicesapi.models.domain.enums.FeeTypeEnum;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Fee {
  private FeeTypeEnum type;
  private boolean isPercent;
  private double magnitude;
  private CurrencyUnitsEnum magnitudeUnits;
  private long minimumCartSubtotal;
  private long maximumCartSubtotal;
  private CurrencyEnum currency;
  private long minimumFeeMagnitude;
  private long maximumFeeMagnitude;
}
