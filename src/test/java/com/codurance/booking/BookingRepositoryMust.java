package com.codurance.booking;

import com.codurance.hotel.Hotel;
import com.codurance.hotel.room.RoomType;
import com.codurance.hotel.room.RoomTypeNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static com.codurance.TestHelper.getDateFrom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingRepositoryMust {

    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository = new BookingRepository();
    }

    @Test
    void successfully_make_a_booking_when_a_room_is_available() {
        Hotel hotel = new Hotel(UUID.randomUUID(), "My hotel")
                .withRoom(1, RoomType.STANDARD);

        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        Booking booking = new Booking(UUID.randomUUID(), hotel.getHotelId(), RoomType.STANDARD, checkIn, checkOut);
        assertDoesNotThrow(() -> bookingRepository.makeBooking(booking, hotel));
    }

    @Test
    void fail_when_room_is_partially_available() {
        Hotel hotel = new Hotel(UUID.randomUUID(), "My hotel")
                .withRoom(1, RoomType.STANDARD);

        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        Booking booking = new Booking(UUID.randomUUID(), hotel.getHotelId(), RoomType.STANDARD, checkIn, checkOut);


        bookingRepository.makeBooking(booking, hotel);

        assertThrows(RoomTypeNotAvailableException.class, () ->
                bookingRepository.makeBooking(booking, hotel)
        );
    }

    @Test
    void fail_when_no_room_is_available() {
        Hotel hotel = new Hotel(UUID.randomUUID(), "My hotel");

        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        Booking booking = new Booking(UUID.randomUUID(), hotel.getHotelId(), RoomType.STANDARD, checkIn, checkOut);
        assertThrows(RoomTypeNotAvailableException.class, () ->
                bookingRepository.makeBooking(booking, hotel)
        );
    }
}