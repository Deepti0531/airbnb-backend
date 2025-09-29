package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.HotelDto;
import com.projects.airBnbApp.dto.HotelInfoDto;
import com.projects.airBnbApp.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    List<HotelDto> getAllHotels();

    HotelInfoDto getHotelInfoById(Long hotelId);
}
