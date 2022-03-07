package com.foodservicesapi.models.domain;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
public class MenuCategory {
    private String name;
    private List<MenuItem> items;
}
