package com.codurance.feature;

import com.codurance.booking.Booking;
import com.codurance.booking.BookingRepository;
import com.codurance.booking.BookingService;
import com.codurance.booking.BookingValidator;
import com.codurance.booking.exception.BookingNotAllowedException;
import com.codurance.company.BookingPolicyRepository;
import com.codurance.company.BookingPolicyService;
import com.codurance.company.CompanyRepository;
import com.codurance.company.CompanyService;
import com.codurance.hotel.HotelRepository;
import com.codurance.hotel.HotelService;
import com.codurance.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.codurance.TestHelper.getDateFrom;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HotelBookingFeature {

    private HotelService hotelService;
    private BookingService bookingService;
    private BookingPolicyService policyService;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        HotelRepository hotelRepository = new HotelRepository();
        hotelService = new HotelService(hotelRepository);

        CompanyRepository companyRepository = new CompanyRepository();
        companyService = new CompanyService(companyRepository);

        BookingPolicyRepository policyRepository = new BookingPolicyRepository();
        policyService = new BookingPolicyService(policyRepository, companyRepository);

        BookingValidator bookingValidator = new BookingValidator(policyService);
        BookingRepository bookingRepository = new BookingRepository();
        bookingService = new BookingService(hotelService, bookingValidator, bookingRepository);
    }

    @Test
    void should_book_a_recently_added_hotel_room() {
        UUID hotelId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        hotelService.addHotel(hotelId, "My wonderful hotel");
        hotelService.setRoom(hotelId, 1, RoomType.STANDARD);

        companyService.addEmployee(companyId, employeeId);

        Booking booking = bookingService.book(employeeId, hotelId, RoomType.STANDARD, checkIn, checkOut);

        assertNotNull(booking.getId());
    }

    @Test
    void should_fail_booking_because_of_company_policy() {
        UUID hotelId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        hotelService.addHotel(hotelId, "My wonderful hotel");
        hotelService.setRoom(hotelId, 1, RoomType.STANDARD);
        hotelService.setRoom(hotelId, 2, RoomType.JUNIOR_SUITE);

        companyService.addEmployee(companyId, employeeId);
        policyService.setCompanyPolicy(companyId, Collections.singleton(RoomType.STANDARD));

        assertThrows(BookingNotAllowedException.class, () ->
                bookingService.book(employeeId, hotelId, RoomType.JUNIOR_SUITE, checkIn, checkOut)
        );
    }

    @Test
    void should_fail_booking_because_of_employee_policy_precedence() {
        UUID hotelId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        LocalDate checkIn = getDateFrom("01-01-2019");
        LocalDate checkOut = getDateFrom("03-01-2019");

        hotelService.addHotel(hotelId, "My wonderful hotel");
        hotelService.setRoom(hotelId, 1, RoomType.STANDARD);
        hotelService.setRoom(hotelId, 2, RoomType.JUNIOR_SUITE);

        companyService.addEmployee(companyId, employeeId);
        policyService.setEmployeePolicy(employeeId, Collections.singleton(RoomType.STANDARD));
        policyService.setCompanyPolicy(companyId, Set.of(RoomType.STANDARD, RoomType.JUNIOR_SUITE));

        assertThrows(BookingNotAllowedException.class, () ->
                bookingService.book(employeeId, hotelId, RoomType.JUNIOR_SUITE, checkIn, checkOut)
        );
    }

    @Test
    void should_book_multiple_rooms_within_policy() {
        UUID hotelId = UUID.randomUUID();
        UUID employeeId1 = UUID.randomUUID();
        UUID employeeId2 = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();

        hotelService.addHotel(hotelId, "My wonderful hotel");
        hotelService.setRoom(hotelId, 1, RoomType.STANDARD);
        hotelService.setRoom(hotelId, 2, RoomType.MASTER_SUITE);

        companyService.addEmployee(companyId, employeeId1);
        companyService.addEmployee(companyId, employeeId2);

        policyService.setCompanyPolicy(companyId, Set.of(RoomType.STANDARD, RoomType.MASTER_SUITE));
        policyService.setEmployeePolicy(employeeId1, Collections.singleton(RoomType.STANDARD));

        Booking booking1 = bookingService.book(employeeId1, hotelId, RoomType.STANDARD, getDateFrom("01-01-2019"), getDateFrom("03-01-2019"));
        Booking booking2 = bookingService.book(employeeId1, hotelId, RoomType.STANDARD, getDateFrom("10-01-2019"), getDateFrom("15-01-2019"));
        Booking booking3 = bookingService.book(employeeId2, hotelId, RoomType.MASTER_SUITE, getDateFrom("01-01-2019"), getDateFrom("03-01-2019"));
        Booking booking4 = bookingService.book(employeeId2, hotelId, RoomType.STANDARD, getDateFrom("20-01-2019"), getDateFrom("23-01-2019"));

        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());
        assertNotNull(booking4.getId());
    }
}
