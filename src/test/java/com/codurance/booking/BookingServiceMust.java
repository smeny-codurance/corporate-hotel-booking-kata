package com.codurance.booking;

import com.codurance.booking.exception.BookingNotAllowedException;
import com.codurance.booking.exception.InvalidCheckInException;
import com.codurance.company.BookingPolicyService;
import com.codurance.hotel.Hotel;
import com.codurance.hotel.HotelService;
import com.codurance.hotel.exception.HotelNotFoundException;
import com.codurance.hotel.room.RoomType;
import com.codurance.hotel.room.RoomTypeNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.codurance.TestHelper.getDateFrom;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingServiceMust {

    private static final UUID HOTEL_ID = UUID.randomUUID();
    private static final UUID EMPLOYEE_ID = UUID.randomUUID();
    private static final RoomType ROOM_TYPE = RoomType.STANDARD;
    private static final LocalDate CHECK_IN = getDateFrom("01-01-2019");
    private static final LocalDate CHECK_OUT = getDateFrom("03-01-2019");

    private BookingService bookingService;

    @Mock
    private HotelService hotelService;
    @Mock
    private BookingPolicyService policyService;
    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        BookingValidator bookingValidator = new BookingValidator(policyService);
        bookingService = new BookingService(hotelService, bookingValidator, bookingRepository);
    }

    @Test
    void return_booking_with_id_and_information() {
        Hotel hotel = new Hotel(HOTEL_ID, "My hotel")
                .withRoom(1, ROOM_TYPE);
        given(hotelService.findBy(HOTEL_ID)).willReturn(
                Optional.of(hotel)
        );
        given(policyService.isBookingAllowed(EMPLOYEE_ID, ROOM_TYPE))
                .willReturn(true);

        Booking booking = bookingService.book(EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECK_IN, CHECK_OUT);

        assertNotNull(booking.getId());
        assertEquals(booking.getEmployeeId(), EMPLOYEE_ID);
        assertEquals(booking.getHotelId(), HOTEL_ID);
        assertEquals(booking.getRoomType(), ROOM_TYPE);
        assertEquals(booking.getCheckIn(), CHECK_IN);
        assertEquals(booking.getCheckOut(), CHECK_OUT);

        verify(bookingRepository).makeBooking(booking, hotel);
    }

    @Test
    void fail_if_check_out_is_before_check_in() {
        LocalDate checkIn = getDateFrom("03-01-2019");
        LocalDate checkOut = getDateFrom("01-01-2019");
        given(hotelService.findBy(HOTEL_ID)).willReturn(
                Optional.of(new Hotel(HOTEL_ID, "My hotel")
                        .withRoom(1, ROOM_TYPE))
        );

        assertThrows(InvalidCheckInException.class, () ->
                bookingService.book(EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, checkIn, checkOut)
        );
    }

    @Test
    void fail_if_hotel_does_not_exist() {
        given(hotelService.findBy(HOTEL_ID)).willReturn(
                Optional.empty()
        );

        assertThrows(HotelNotFoundException.class, () ->
                bookingService.book(UUID.randomUUID(), HOTEL_ID, ROOM_TYPE, CHECK_IN, CHECK_OUT)
        );
    }

    @Test
    void fail_if_room_type_is_not_available() {
        given(hotelService.findBy(HOTEL_ID)).willReturn(
                Optional.of(new Hotel(HOTEL_ID, "My hotel")
                        .withRoom(1, ROOM_TYPE))
        );

        assertThrows(RoomTypeNotAvailableException.class, () ->
                bookingService.book(EMPLOYEE_ID, HOTEL_ID, RoomType.JUNIOR_SUITE, CHECK_IN, CHECK_OUT)
        );
    }

    @Test
    void fail_if_booking_not_allowed_by_company_policies() {
        given(hotelService.findBy(HOTEL_ID)).willReturn(
                Optional.of(new Hotel(HOTEL_ID, "My hotel")
                        .withRoom(1, ROOM_TYPE))
        );
        given(policyService.isBookingAllowed(EMPLOYEE_ID, ROOM_TYPE))
                .willReturn(false);

        assertThrows(BookingNotAllowedException.class, () ->
                bookingService.book(EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECK_IN, CHECK_OUT)
        );
    }

}