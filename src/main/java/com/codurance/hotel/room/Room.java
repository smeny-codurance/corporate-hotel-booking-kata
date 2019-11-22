package com.codurance.hotel.room;

import java.util.Objects;
import java.util.UUID;

public class Room {

    private final int roomNumber;
    private final RoomType roomType;
    private final UUID hotelId;

    public Room(int roomNumber, RoomType roomType, UUID hotelId) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.hotelId = hotelId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber &&
                hotelId.equals(room.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, hotelId);
    }
}
