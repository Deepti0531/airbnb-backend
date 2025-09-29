package com.projects.airBnbApp.dto;

import com.projects.airBnbApp.entity.Guest;
import com.projects.airBnbApp.entity.Hotel;
import com.projects.airBnbApp.entity.Room;
import com.projects.airBnbApp.entity.User;
import com.projects.airBnbApp.enums.BookingStatus;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;
    private Hotel hotel;
    private Room room;
    private User user;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
