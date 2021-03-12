package ftc.shift.sample.facade;

import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.mapper.CustomerMapper;
import ftc.shift.sample.service.ContactService;
import ftc.shift.sample.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class CustomerFacadeTest {

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerMapper customerMapper;

    @MockBean
    private ContactService contactService;

    private CustomerFacade customerFacade;

    @BeforeEach
    void setUp() {
        customerFacade = new CustomerFacade(customerService, customerMapper, contactService);
    }

    @Test
    void createCustomer() {
        CustomerDtoRequest customerDtoRequest = new CustomerDtoRequest();
        Customer customer = new Customer();

        when(customerMapper.dtoRequestToCustomer(customerDtoRequest)).thenReturn(customer);
        when(customerService.createCustomer(customer)).thenReturn(customer);

        customerFacade.createCustomer(customerDtoRequest);
        verify(customerMapper, times(1)).dtoRequestToCustomer(customerDtoRequest);
        verify(customerService, times(1)).createCustomer(any(Customer.class));
        verify(customerMapper, times(1)).customerToDtoResponse(any(Customer.class));
    }

    @Test
    void getCustomer() throws DataNotFoundException {
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse();
        customerDtoResponse.setId(id);

        List<String> contactsString = new ArrayList<>();
        contactsString.add("email@gmail.com");
        customerDtoResponse.setEmails(contactsString);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("email@gmail.com", customer));
        customer.setContact(contacts);

        when(contactService.getAllEmails(customer.getId())).thenReturn(contactsString);
        when(customerMapper.customerToDtoResponse(customer)).thenReturn(customerDtoResponse);
        when(customerService.getCustomer(customer.getId())).thenReturn(customer);

        customerFacade.getCustomer(customer.getId());
        verify(customerService, times(1)).getCustomer(customer.getId());
        verify(customerMapper, times(1)).customerToDtoResponse(customer);
    }

    @Test
    void updateCustomer() throws DataNotFoundException {
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse();
        customerDtoResponse.setId(id);

        CustomerDtoRequest customerDtoRequest = new CustomerDtoRequest();

        List<String> contactsString = new ArrayList<>();
        contactsString.add("email@gmail.com");
        customerDtoResponse.setEmails(contactsString);
        customerDtoRequest.setEmails(contactsString);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("email@gmail.com", customer));
        customer.setContact(contacts);

        when(contactService.getAllEmails(customer.getId())).thenReturn(contactsString);

        when(customerMapper.dtoRequestToCustomer(customerDtoRequest)).thenReturn(customer);
        when(customerService.updateCustomer(customer, id)).thenReturn(customer);
        when(customerMapper.customerToDtoResponse(customer)).thenReturn(customerDtoResponse);

        customerFacade.updateCustomer(customerDtoRequest, id);

        verify(customerMapper, times(1)).dtoRequestToCustomer(customerDtoRequest);
        verify(customerService, times(1)).updateCustomer(customer, id);
        verify(customerMapper, times(1)).customerToDtoResponse(customer);
    }

    @Test
    void deleteCustomer() {
        Long id = 1L;
        customerFacade.deleteCustomer(id);
        verify(customerService, times(1)).deleteCustomer(id);
    }

    @Test
    void getAllCustomers() {
        List<Customer> customerList = new LinkedList<>();
        customerList.add(new Customer());
        customerList.add(new Customer());
        customerList.add(new Customer());

        when(customerService.getAllCustomers()).thenReturn(customerList);
        List<CustomerDtoResponse> customerDtoResponseList = customerFacade.getAllCustomers();
        assertEquals(customerList.size(), customerDtoResponseList.size());
    }
}