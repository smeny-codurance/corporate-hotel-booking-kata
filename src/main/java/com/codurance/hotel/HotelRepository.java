package com.codurance.hotel;

import com.codurance.hotel.room.RoomType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HotelRepository {

    private final Map<UUID, Hotel> hotelMap;

    public HotelRepository() {
        this.hotelMap = new HashMap<>();
    }

    void createHotel(UUID hotelId, String hotelName) {
        Hotel hotel = new Hotel(hotelId, hotelName);
        hotelMap.put(hotelId, hotel);
    }


    Hotel findById(UUID hotelId) {
        return hotelMap.get(hotelId);
    }

    void addOrUpdateHotelRoom(UUID hotelId, int roomNumber, RoomType roomType) {
        findById(hotelId).addOrUpdateRoom(roomNumber, roomType);
    }
}
