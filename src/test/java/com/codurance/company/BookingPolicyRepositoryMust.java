package com.codurance.company;

import com.codurance.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

class BookingPolicyRepositoryMust {

    private BookingPolicyRepository policyRepository;

    @BeforeEach
    void setUp() {
        policyRepository = new BookingPolicyRepository();
    }

    @Test
    void return_an_empty_room_type_set_when_no_employee_policy_exists() {
        assertThat(policyRepository.getEmployeePolicy(UUID.randomUUID()), hasSize(0));
    }

    @Test
    void return_an_empty_room_type_set_when_no_company_policy_exists() {
        assertThat(policyRepository.getCompanyPolicy(UUID.randomUUID()), hasSize(0));
    }

    @Test
    void create_a_new_employee_policy() {
        UUID employeeId = UUID.randomUUID();

        policyRepository.createOrUpdateEmployeePolicy(employeeId, Collections.singleton(RoomType.STANDARD));

        assertThat(policyRepository.getEmployeePolicy(employeeId), contains(RoomType.STANDARD));
    }

    @Test
    void create_a_new_company_policy() {
        UUID employeeId = UUID.randomUUID();

        policyRepository.createOrUpdateCompanyPolicy(employeeId, Collections.singleton(RoomType.STANDARD));

        assertThat(policyRepository.getCompanyPolicy(employeeId), contains(RoomType.STANDARD));
    }

    @Test
    void update_and_replace_an_existing_employee_policy() {
        UUID employeeId = UUID.randomUUID();
        policyRepository.createOrUpdateEmployeePolicy(employeeId, Collections.singleton(RoomType.STANDARD));

        policyRepository.createOrUpdateEmployeePolicy(employeeId, Collections.singleton(RoomType.JUNIOR_SUITE));

        assertThat(policyRepository.getEmployeePolicy(employeeId), contains(RoomType.JUNIOR_SUITE));
    }

    @Test
    void update_and_replace_an_existing_company_policy() {
        UUID employeeId = UUID.randomUUID();
        policyRepository.createOrUpdateCompanyPolicy(employeeId, Collections.singleton(RoomType.STANDARD));

        policyRepository.createOrUpdateCompanyPolicy(employeeId, Collections.singleton(RoomType.JUNIOR_SUITE));

        assertThat(policyRepository.getCompanyPolicy(employeeId), contains(RoomType.JUNIOR_SUITE));
    }

}