package ftc.shift.sample.service;

import ftc.shift.sample.filter.SearchCriteria;
import ftc.shift.sample.mapper.CustomerMapper;
import ftc.shift.sample.mapper.LicenceMapper;
import ftc.shift.sample.repository.ContactRepository;
import ftc.shift.sample.repository.CustomerRepository;
import ftc.shift.sample.repository.LicenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private LicenceRepository licenceRepository;

    @MockBean
    private CustomerMapper customerMapper;

    @MockBean
    private LicenceMapper licenceMapper;

    @MockBean
    private ContactRepository contactRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(
                customerRepository,
                licenceRepository,
                customerMapper,
                licenceMapper,
                contactRepository);
    }

    @Test
    void getAllUsersWithFilter() {
        employeeService.getAllCustomersWithFilter(List.of(new SearchCriteria()));
        verify(customerRepository, times(1)).findAll();
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKey("id");
        criteria.setOperation("=");
        criteria.setValue("1");
        employeeService.getAllCustomersWithFilter(List.of(criteria));
        verify(customerRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getUsersCountWithFilter() {
        employeeService.getCustomersCountWithFilter(List.of(new SearchCriteria()));
        verify(customerRepository, times(1)).count();
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKey("id");
        criteria.setOperation("=");
        criteria.setValue("1");
        employeeService.getCustomersCountWithFilter(List.of(criteria));
        verify(customerRepository, times(1)).count(any(Specification.class));
    }

    @Test
    void getAllLicencesWithFilter() {
        employeeService.getAllLicencesWithFilter(List.of(new SearchCriteria()));
        verify(licenceRepository, times(1)).findAll();
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKey("customer");
        criteria.setOperation("=");
        criteria.setValue("1");
        employeeService.getAllLicencesWithFilter(List.of(criteria));
        verify(licenceRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getLicencesCountWithFilter() {
        employeeService.getLicencesCountWithFilter(List.of(new SearchCriteria()));
        verify(licenceRepository, times(1)).count();
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKey("customer");
        criteria.setOperation("=");
        criteria.setValue("1");
        employeeService.getLicencesCountWithFilter(List.of(criteria));
        verify(licenceRepository, times(1)).count(any(Specification.class));
    }
}