package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.RoomDto;
import com.projects.airBnbApp.entity.Hotel;
import com.projects.airBnbApp.entity.Room;
import com.projects.airBnbApp.repository.HotelRepository;
import com.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImplementation implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new room in hotel with id: {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel with id: " + hotelId + " not found"));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);


        }
        return modelMapper.map(room, RoomDto.class);

    }

    @Override
    public List<RoomDto> getAllRoomsInHotel( Long hotelId) {
        log.info("Getting all rooms in hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel with id: " + hotelId + " not found"));
        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class)).collect(Collectors.toList());


    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room with id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with id: " + roomId + " not found"));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with id: " + roomId + " not found"));

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);

    }

    @Override
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating room with ID: {} in hotel with ID: {}", roomId, hotelId);

        // Fetch the hotel
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        // Fetch the room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        // Update fields using your entity's actual field names
        room.setType(roomDto.getType());
        room.setCapacity(roomDto.getCapacity());
        room.setBasePrice(roomDto.getBasePrice());
        room.setTotalCount(roomDto.getTotalCount());
        room.setPhotos(roomDto.getPhotos());
        room.setAmenities(roomDto.getAmenities());

        // Save updated room
        Room updatedRoom = roomRepository.save(room);

        // If the room's totalCount > 0, we can consider it active and update inventory
        if (updatedRoom.getTotalCount() > 0) {
            inventoryService.initializeRoomForAYear(updatedRoom);
        }

        return modelMapper.map(updatedRoom, RoomDto.class);
    }






}
