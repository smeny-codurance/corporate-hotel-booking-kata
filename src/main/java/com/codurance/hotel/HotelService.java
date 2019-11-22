package com.codurance.hotel;

import com.codurance.hotel.exception.HotelAlreadyExistsException;
import com.codurance.hotel.exception.HotelNotFoundException;
import com.codurance.hotel.room.RoomType;

import java.util.Optional;
import java.util.UUID;

public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void addHotel(UUID hotelId, String hotelName) {
        if (hotelExists(hotelId)) {
            String errorMessage = String.format("Hotel with ID %s already exists", hotelId);
            throw new HotelAlreadyExistsException(errorMessage);
        } else {
            hotelRepository.createHotel(hotelId, hotelName);
        }
    }

    public void setRoom(UUID hotelId, int roomNumber, RoomType roomType) {
        if (hotelExists(hotelId)) {
            hotelRepository.addOrUpdateHotelRoom(hotelId, roomNumber, roomType);
        } else {
            String errorMessage = String.format("Hotel with ID %s does not exist", hotelId);
            throw new HotelNotFoundException(errorMessage);
        }
    }

    public Optional<Hotel> findBy(UUID hotelId) {
        return Optional.ofNullable(hotelRepository.findById(hotelId));
    }

    private boolean hotelExists(UUID hotelId) {
        return findBy(hotelId).isPresent();
    }

}
