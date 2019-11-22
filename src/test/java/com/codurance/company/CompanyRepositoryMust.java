package com.codurance.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

class CompanyRepositoryMust {

    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        companyRepository = new CompanyRepository();
    }

    @Test
    void store_an_employee_company_id() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        companyRepository.addEmployee(companyId, employeeId);

        assertThat(companyRepository.getCompanyIdForEmployee(employeeId), is(companyId));
    }

    @Test
    void delete_an_employee_company_id() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        companyRepository.addEmployee(companyId, employeeId);
        companyRepository.deleteEmployee(employeeId);

        assertThat(companyRepository.getCompanyIdForEmployee(employeeId), nullValue());
    }
}