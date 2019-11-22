package com.codurance.booking;

import com.codurance.hotel.Hotel;
import com.codurance.hotel.HotelService;
import com.codurance.hotel.exception.HotelNotFoundException;
import com.codurance.hotel.room.RoomType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class BookingService {

    private final HotelService hotelService;
    private final BookingValidator bookingValidator;
    private final BookingRepository bookingRepository;

    public BookingService(HotelService hotelService, BookingValidator bookingValidator, BookingRepository bookingRepository) {
        this.hotelService = hotelService;
        this.bookingValidator = bookingValidator;
        this.bookingRepository = bookingRepository;
    }

    public Booking book(UUID employeeId, UUID hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        Hotel hotel = retrieveHotelOrThrowException(hotelId);
        Booking booking = new Booking(employeeId, hotelId, roomType, checkIn, checkOut);

        bookingValidator.validateBookingRequest(booking, hotel);
        bookingRepository.makeBooking(booking, hotel);
        return booking;
    }

    private Hotel retrieveHotelOrThrowException(UUID hotelId) {
        Optional<Hotel> hotel = hotelService.findBy(hotelId);

        String errorMessage = String.format("Hotel with ID %s does not exist", hotelId);
        return hotel.orElseThrow(() -> new HotelNotFoundException(errorMessage));
    }

}
