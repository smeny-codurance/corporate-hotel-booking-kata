package com.codurance.company;

import com.codurance.company.exception.EmployeeAlreadyExistsException;

import java.util.UUID;

public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void addEmployee(UUID companyId, UUID employeeId) {
        if (companyRepository.getCompanyIdForEmployee(employeeId) != null) {
            throw new EmployeeAlreadyExistsException();
        }

        companyRepository.addEmployee(companyId, employeeId);
    }

    public void deleteEmployee(UUID employeeId) {
        companyRepository.deleteEmployee(employeeId);
    }

}
