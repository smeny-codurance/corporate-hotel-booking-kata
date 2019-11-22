package com.codurance.company;

import com.codurance.hotel.room.RoomType;

import java.util.*;

public class BookingPolicyRepository {

    private final Map<UUID, Set<RoomType>> employeePolicies;
    private final Map<UUID, Set<RoomType>> companyPolicies;

    public BookingPolicyRepository() {
        this.employeePolicies = new HashMap<>();
        this.companyPolicies = new HashMap<>();
    }

    public Set<RoomType> getEmployeePolicy(UUID employeeId) {
        return employeePolicies.computeIfAbsent(employeeId, k -> new HashSet<>());
    }

    public Set<RoomType> getCompanyPolicy(UUID companyId) {
        return companyPolicies.computeIfAbsent(companyId, k -> new HashSet<>());
    }

    public void createOrUpdateEmployeePolicy(UUID employeeId, Set<RoomType> allowedRoomTypes) {
        employeePolicies.put(employeeId, allowedRoomTypes);
    }

    public void createOrUpdateCompanyPolicy(UUID companyId, Set<RoomType> allowedRoomTypes) {
        companyPolicies.put(companyId, allowedRoomTypes);
    }

}
