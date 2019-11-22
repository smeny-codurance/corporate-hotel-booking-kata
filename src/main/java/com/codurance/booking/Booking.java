package com.codurance.booking;

import com.codurance.hotel.room.RoomType;

import java.time.LocalDate;
import java.util.UUID;

public class Booking {

    private final UUID id;
    private final UUID employeeId;
    private final UUID hotelId;
    private final RoomType roomType;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public Booking(UUID employeeId, UUID hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        this.id = UUID.randomUUID();
        this.employeeId = employeeId;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public UUID getId() {
        return id;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public UUID getHotelId() {
        return hotelId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}
