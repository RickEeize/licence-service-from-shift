package ftc.shift.sample.service;

import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.repository.CustomerRepository;
import ftc.shift.sample.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @MockBean
    private CustomerRepository repository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(repository);
    }

    @Test
    void createCustomer() {
        customerService.createCustomer(new Customer());
        verify(repository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomer() throws DataNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);

        when(repository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        Customer result = customerService.getCustomer(customer.getId());

        verify(repository, times(1)).findById(customer.getId());
        assertEquals(result, customer);
    }

    @Test
    void updateCustomer() throws DataNotFoundException {
        Date registrationDate = Date.valueOf(LocalDate.now());
        List<Contact> contacts = new ArrayList<>();
        Customer customer = new Customer(Constants.CUSTOMER_TYPE_COMPANY, "Tony", registrationDate, contacts);
        customer.setId(1L);

        contacts.add(new Contact("email@gmail.com", customer));
        Customer updatedCustomer = new Customer(Constants.CUSTOMER_TYPE_USER, "Tony", registrationDate, contacts);

        when(repository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        when(repository.save(customer)).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(updatedCustomer, customer.getId());
        verify(repository, times(1)).findById(customer.getId());
        verify(repository, times(1)).save(customer);

        assertEquals(updatedCustomer, result);
    }

    @Test
    void deleteCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(repository.getOne(customer.getId())).thenReturn(customer);

        customerService.deleteCustomer(customer.getId());

        verify(repository, times(1)).getOne(customer.getId());
        verify(repository, times(1)).delete(customer);
    }

    @Test
    void getAllCustomers() {
        customerService.getAllCustomers();
        verify(repository, times(1)).findAll();
    }
}