package com.codurance.company;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompanyRepository {

    private final Map<UUID, UUID> companyByEmployeeId;

    public CompanyRepository() {
        companyByEmployeeId = new HashMap<>();
    }

    public UUID getCompanyIdForEmployee(UUID employeeId) {
        return companyByEmployeeId.get(employeeId);
    }

    public void addEmployee(UUID companyId, UUID employeeId) {
        companyByEmployeeId.put(employeeId, companyId);
    }

    public void deleteEmployee(UUID employeeId) {
        companyByEmployeeId.remove(employeeId);
    }
}
