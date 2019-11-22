package com.codurance.hotel;

import com.codurance.hotel.exception.HotelAlreadyExistsException;
import com.codurance.hotel.exception.HotelNotFoundException;
import com.codurance.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HotelServiceMust {

    private static final String HOTEL_NAME = "New hotel";

    @Mock
    private HotelRepository hotelRepository;

    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(hotelRepository);
    }

    @Test
    void add_new_hotel_to_repository() {
        UUID hotelId = UUID.randomUUID();

        hotelService.addHotel(hotelId, HOTEL_NAME);

        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository).createHotel(hotelId, HOTEL_NAME);
    }

    @Test
    void fail_if_hotel_exists_when_adding_new_hotel() {
        UUID hotelId = UUID.randomUUID();
        givenHotelExists(hotelId);

        assertThrows(HotelAlreadyExistsException.class, () ->
                hotelService.addHotel(hotelId, HOTEL_NAME)
        );
    }

    @Test
    void add_or_update_new_room_if_hotel_exists() {
        UUID hotelId = UUID.randomUUID();
        givenHotelExists(hotelId);

        hotelService.setRoom(hotelId, 25, RoomType.STANDARD);

        verify(hotelRepository).addOrUpdateHotelRoom(hotelId, 25, RoomType.STANDARD);
    }

    @Test
    void fail_when_adding_or_updating_a_room_for_unknown_hotel() {
        assertThrows(HotelNotFoundException.class, () ->
                hotelService.setRoom(UUID.randomUUID(), 25, RoomType.STANDARD)
        );
    }

    @Test
    void find_an_existing_hotel() {
        UUID hotelId = UUID.randomUUID();
        givenHotelExists(hotelId);

        assertTrue(hotelService.findBy(hotelId).isPresent());
    }

    @Test
    void return_empty_result_for_unknown_hotels() {
        assertTrue(hotelService.findBy(UUID.randomUUID()).isEmpty());
    }

    private void givenHotelExists(UUID hotelId) {
        Hotel hotel = new Hotel(hotelId, HOTEL_NAME);
        given(hotelRepository.findById(hotelId)).willReturn(hotel);
    }
}