package com.codurance.company;

import com.codurance.company.exception.EmployeeAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceMust {

    @Mock
    private CompanyRepository companyRepository;

    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository);
    }

    @Test
    void add_an_employee_to_a_company_repository() {
        UUID companyId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        companyService.addEmployee(companyId, employeeId);

        verify(companyRepository).addEmployee(companyId, employeeId);
    }

    @Test
    void prevent_employee_duplication() {
        UUID employeeId = UUID.randomUUID();
        given(companyRepository.getCompanyIdForEmployee(employeeId))
                .willReturn(UUID.randomUUID());

        assertThrows(EmployeeAlreadyExistsException.class, () ->
                companyService.addEmployee(UUID.randomUUID(), employeeId)
        );
    }

    @Test
    void delete_an_employee_from_company_repository() {
        UUID employeeId = UUID.randomUUID();

        companyService.deleteEmployee(employeeId);

        verify(companyRepository).deleteEmployee(employeeId);
    }
}