package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.HotelDto;
import com.projects.airBnbApp.dto.HotelInfoDto;
import com.projects.airBnbApp.dto.RoomDto;
import com.projects.airBnbApp.entity.Hotel;
import com.projects.airBnbApp.entity.Room;
import com.projects.airBnbApp.exception.ResourceNotFoundException;
import com.projects.airBnbApp.repository.HotelRepository;
import com.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class HotelServiceImplementation implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    LocalDateTime today = LocalDateTime.now();
    LocalDateTime endDate = today.plusYears(1);

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto){
        log.info("Creating a new Hotel with name: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Hotel with id: {} has been created", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id){
       log.info("Getting Hotel with id: {}", id);
       Hotel hotel = hotelRepository
               .findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID:"+id));
       return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto){
        log.info("Updating Hotel with id: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID:"+id));

        // map fields except ID
        Long hotelId = hotel.getId();  // keep the existing id
        modelMapper.map(hotelDto, hotel);
        hotel.setId(hotelId);          // restore id just in case

        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }


    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        boolean exists = hotelRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Hotel not found with ID:" + id);
        }


        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID:"+id));

        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId){
        log.info("Activating Hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID:"+hotelId));
        hotel.setActive(true);
        //assuminh only do it once
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

    }

    @Override
    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .toList();
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId){
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID:"+hotelId));
                List<RoomDto> rooms = hotel.getRooms()
                        .stream()
                        .map((element) -> modelMapper.map(element, RoomDto.class))
                        .toList();
                return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);

    }









}
