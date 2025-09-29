package com.projects.airBnbApp.service;


import com.projects.airBnbApp.dto.HotelDto;
import com.projects.airBnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);
    List<RoomDto> getAllRoomsInHotel(Long  hotelId);
    RoomDto getRoomById(Long id);
    void deleteRoomById(Long id);
    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
