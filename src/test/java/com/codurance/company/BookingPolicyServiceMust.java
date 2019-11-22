package com.codurance.company;

import com.codurance.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingPolicyServiceMust {

    @Mock
    private BookingPolicyRepository policyRepository;
    @Mock
    private CompanyRepository companyRepository;

    private BookingPolicyService policyService;

    @BeforeEach
    void setUp() {
        policyService = new BookingPolicyService(policyRepository, companyRepository);
    }

    @Test
    void allow_booking_when_no_policy_exist() {
        assertTrue(policyService.isBookingAllowed(UUID.randomUUID(), RoomType.STANDARD));
    }

    @Test
    void allow_booking_when_employee_policy_matches() {
        UUID employeeId = UUID.randomUUID();
        given(policyRepository.getEmployeePolicy(employeeId))
                .willReturn(Collections.singleton(RoomType.STANDARD));

        assertTrue(policyService.isBookingAllowed(employeeId, RoomType.STANDARD));
    }

    @Test
    void allow_booking_when_company_policy_matches() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        given(companyRepository.getCompanyIdForEmployee(employeeId))
                .willReturn(companyId);
        given(policyRepository.getCompanyPolicy(companyId))
                .willReturn(Collections.singleton(RoomType.STANDARD));

        assertTrue(policyService.isBookingAllowed(employeeId, RoomType.STANDARD));
    }

    @Test
    void prevent_booking_when_employee_policy_is_not_respected() {
        UUID employeeId = UUID.randomUUID();
        given(policyRepository.getEmployeePolicy(employeeId))
                .willReturn(Collections.singleton(RoomType.STANDARD));

        assertFalse(policyService.isBookingAllowed(employeeId, RoomType.JUNIOR_SUITE));
    }

    @Test
    void prevent_booking_when_company_policy_is_not_respected() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        given(companyRepository.getCompanyIdForEmployee(employeeId))
                .willReturn(companyId);
        given(policyRepository.getCompanyPolicy(companyId))
                .willReturn(Collections.singleton(RoomType.STANDARD));

        assertFalse(policyService.isBookingAllowed(employeeId, RoomType.JUNIOR_SUITE));
    }

    @Test
    void ensure_employee_policy_take_precedence() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        given(companyRepository.getCompanyIdForEmployee(employeeId))
                .willReturn(companyId);
        given(policyRepository.getEmployeePolicy(employeeId))
                .willReturn(Collections.singleton(RoomType.STANDARD));
        given(policyRepository.getCompanyPolicy(companyId))
                .willReturn(Set.of(RoomType.STANDARD, RoomType.JUNIOR_SUITE));

        assertFalse(policyService.isBookingAllowed(employeeId, RoomType.JUNIOR_SUITE));
    }

    @Test
    void set_employee_policy_should_create_policy() {
        UUID employeeId = UUID.randomUUID();
        Set<RoomType> allowedRoomTypes = Collections.singleton(RoomType.STANDARD);

        policyService.setEmployeePolicy(employeeId, allowedRoomTypes);

        verify(policyRepository).createOrUpdateEmployeePolicy(employeeId, allowedRoomTypes);
    }

    @Test
    void set_company_policy_should_create_policy() {
        UUID companyId = UUID.randomUUID();
        Set<RoomType> allowedRoomTypes = Collections.singleton(RoomType.STANDARD);

        policyService.setCompanyPolicy(companyId, allowedRoomTypes);

        verify(policyRepository).createOrUpdateCompanyPolicy(companyId, allowedRoomTypes);
    }

}