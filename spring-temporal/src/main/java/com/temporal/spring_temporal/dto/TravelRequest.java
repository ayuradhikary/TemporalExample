package com.temporal.spring_temporal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelRequest {
    private String userId;
    private String destination;
    private String travelDate;
}
