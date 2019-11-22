package com.codurance.hotel;

import com.codurance.hotel.room.Room;
import com.codurance.hotel.room.RoomType;

import java.util.*;

public class Hotel {
    private final UUID hotelId;
    private final String hotelName;
    private final Set<Room> rooms;

    public Hotel(UUID hotelId, String hotelName) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.rooms = new HashSet<>();
    }

    public Hotel withRoom(int roomNumber, RoomType roomType) {
        rooms.add(new Room(roomNumber, roomType, hotelId));
        return this;
    }

    public UUID getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public Set<Room> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

    void addOrUpdateRoom(int roomNumber, RoomType roomType) {
        Room newOrUpdatedRoom = new Room(roomNumber, roomType, hotelId);
        rooms.remove(newOrUpdatedRoom);
        rooms.add(newOrUpdatedRoom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return hotelId.equals(hotel.hotelId) &&
                hotelName.equals(hotel.hotelName) &&
                rooms.equals(hotel.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelId, hotelName, rooms);
    }
}
