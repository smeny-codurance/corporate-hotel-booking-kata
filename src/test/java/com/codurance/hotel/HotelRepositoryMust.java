package com.codurance.hotel;

import com.codurance.hotel.room.Room;
import com.codurance.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HotelRepositoryMust {

    private static final String HOTEL_NAME = "New hotel";

    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository = new HotelRepository();
    }

    @Test
    void return_null_when_hotel_id_cannot_be_found() {
        assertNull(hotelRepository.findById(UUID.randomUUID()));
    }

    @Test
    void find_hotel_after_being_created() {
        Hotel expectedHotel = new Hotel(UUID.randomUUID(), HOTEL_NAME);

        Hotel actualHotel = createAndFindHotel(expectedHotel.getHotelId(), expectedHotel.getHotelName());

        assertEquals(expectedHotel, actualHotel);
    }

    @Test
    void add_new_room_to_hotel() {
        UUID hotelId = UUID.randomUUID();
        Room expectedRoom = new Room(25, RoomType.STANDARD, hotelId);

        Hotel hotel = createAndFindHotel(hotelId, HOTEL_NAME);
        hotelRepository.addOrUpdateHotelRoom(hotelId, expectedRoom.getRoomNumber(), expectedRoom.getRoomType());

        assertThat(hotel.getRooms(), hasSize(1));
        assertThat(hotel.getRooms(), contains(expectedRoom));
    }

    @Test
    void update_existing_room() {
        UUID hotelId = UUID.randomUUID();
        Room expectedRoom = new Room(25, RoomType.JUNIOR_SUITE, hotelId);

        Hotel hotel = createAndFindHotel(hotelId, HOTEL_NAME);
        hotelRepository.addOrUpdateHotelRoom(hotelId, expectedRoom.getRoomNumber(), RoomType.STANDARD);
        hotelRepository.addOrUpdateHotelRoom(hotelId, expectedRoom.getRoomNumber(), expectedRoom.getRoomType());

        assertThat(hotel.getRooms(), hasSize(1));
        assertThat(hotel.getRooms(), contains(expectedRoom));
    }

    private Hotel createAndFindHotel(UUID hotelId, String hotelName) {
        hotelRepository.createHotel(hotelId, hotelName);
        return hotelRepository.findById(hotelId);
    }


}