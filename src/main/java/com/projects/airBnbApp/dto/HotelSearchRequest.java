package com.projects.airBnbApp.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {
    private String city;
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
    private Integer roomsCount;

    private Integer page;
    private Integer size = 10;


}
