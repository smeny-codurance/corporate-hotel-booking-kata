package com.codurance.company;

import com.codurance.hotel.room.RoomType;

import java.util.Set;
import java.util.UUID;

public class BookingPolicyService {

    private final BookingPolicyRepository policyRepository;
    private final CompanyRepository companyRepository;

    public BookingPolicyService(BookingPolicyRepository policyRepository, CompanyRepository companyRepository) {
        this.policyRepository = policyRepository;
        this.companyRepository = companyRepository;
    }

    public void setCompanyPolicy(UUID companyId, Set<RoomType> roomTypes) {
        policyRepository.createOrUpdateCompanyPolicy(companyId, roomTypes);
    }

    public void setEmployeePolicy(UUID employeeId, Set<RoomType> roomTypes) {
        policyRepository.createOrUpdateEmployeePolicy(employeeId, roomTypes);
    }

    public boolean isBookingAllowed(UUID employeeId, RoomType roomType) {
        UUID companyId = companyRepository.getCompanyIdForEmployee(employeeId);
        Set<RoomType> employeePolicy = policyRepository.getEmployeePolicy(employeeId);
        Set<RoomType> companyPolicy = policyRepository.getCompanyPolicy(companyId);

        return isRoomTypeAllowedInPolicy(roomType, employeePolicy) && isRoomTypeAllowedInPolicy(roomType, companyPolicy);
    }

    private boolean isRoomTypeAllowedInPolicy(RoomType roomType, Set<RoomType> policy) {
        return policy.isEmpty() || policy.contains(roomType);
    }

}
