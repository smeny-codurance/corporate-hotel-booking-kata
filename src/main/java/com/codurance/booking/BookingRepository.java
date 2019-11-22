package com.codurance.booking;

import com.codurance.hotel.Hotel;
import com.codurance.hotel.room.Room;
import com.codurance.hotel.room.RoomTypeNotAvailableException;

import java.time.LocalDate;
import java.util.*;

public class BookingRepository {

    private final Map<LocalDate, Set<Room>> roomBookingSchedule;

    public BookingRepository() {
        this.roomBookingSchedule = new HashMap<>();
    }

    public void makeBooking(Booking booking, Hotel hotel) {
        Room availableRoom = findAvailableRoom(booking, hotel.getRooms())
                .orElseThrow(RoomTypeNotAvailableException::new);
        bookRoom(booking.getCheckIn(), booking.getCheckOut(), availableRoom);
    }

    private void bookRoom(LocalDate checkIn, LocalDate checkOut, Room availableRoom) {
        for (LocalDate currentDay = checkIn; currentDay.isBefore(checkOut); currentDay = currentDay.plusDays(1)) {
            Set<Room> bookedRooms = roomBookingSchedule.computeIfAbsent(currentDay, k -> new HashSet<>());
            bookedRooms.add(availableRoom);
        }
    }

    private Optional<Room> findAvailableRoom(Booking booking, Set<Room> hotelRooms) {
        if (hotelRooms.isEmpty()) {
            return Optional.empty();
        }

        Set<Room> availableRooms = new HashSet<>(hotelRooms);
        for (LocalDate currentDay = booking.getCheckIn(); currentDay.isBefore(booking.getCheckOut()); currentDay = currentDay.plusDays(1)) {
            Set<Room> bookedRooms = roomBookingSchedule.computeIfAbsent(currentDay, k -> new HashSet<>());
            availableRooms.removeAll(bookedRooms);
        }

        return availableRooms.stream()
                .filter(availableRoom -> availableRoom.getRoomType().equals(booking.getRoomType()))
                .findAny();
    }
}
