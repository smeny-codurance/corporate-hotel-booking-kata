package com.codurance.hotel.exception;

public class HotelAlreadyExistsException extends RuntimeException {

    public HotelAlreadyExistsException(String message) {
        super(message);
    }
}
