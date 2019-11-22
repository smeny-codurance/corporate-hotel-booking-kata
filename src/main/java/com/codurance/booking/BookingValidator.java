package com.codurance.booking;

import com.codurance.booking.exception.BookingNotAllowedException;
import com.codurance.booking.exception.InvalidCheckInException;
import com.codurance.company.BookingPolicyService;
import com.codurance.hotel.Hotel;
import com.codurance.hotel.room.Room;
import com.codurance.hotel.room.RoomType;
import com.codurance.hotel.room.RoomTypeNotAvailableException;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Predicate;

public class BookingValidator {

    private final BookingPolicyService policyService;

    public BookingValidator(BookingPolicyService policyService) {
        this.policyService = policyService;
    }

    void validateBookingRequest(Booking booking, Hotel hotel) {
        checkHotelProvidesRoomType(hotel, booking.getRoomType());
        validateBookingDates(booking.getCheckIn(), booking.getCheckOut());
        checkCompanyPolicies(booking.getEmployeeId(), booking.getRoomType());
    }

    private void checkHotelProvidesRoomType(Hotel hotel, RoomType requestedRoomType) {
        hotel.getRooms()
                .stream()
                .filter(roomMatches(requestedRoomType))
                .findAny()
                .orElseThrow(RoomTypeNotAvailableException::new);
    }

    private Predicate<Room> roomMatches(RoomType roomType) {
        return room -> room.getRoomType().equals(roomType);
    }

    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new InvalidCheckInException();
        }
    }

    private void checkCompanyPolicies(UUID employeeId, RoomType roomType) {
        if (!policyService.isBookingAllowed(employeeId, roomType)) {
            throw new BookingNotAllowedException();
        }
    }
}