package ftc.shift.sample.facade;

import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.mapper.CustomerMapper;
import ftc.shift.sample.service.ContactService;
import ftc.shift.sample.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerFacade {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    private final ContactService contactService;

    @Autowired
    public CustomerFacade(CustomerService customerService, CustomerMapper customerMapper, ContactService contactService) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.contactService = contactService;
    }

    public CustomerDtoResponse createCustomer(CustomerDtoRequest customer) {
        Customer creatingCustomer = customerMapper.dtoRequestToCustomer(customer);
        Customer createdCustomer = customerService.createCustomer(creatingCustomer);
        contactService.addEmails(customer.getEmails(), createdCustomer);

        return customerMapper.customerToDtoResponse(createdCustomer);
    }

    public CustomerDtoResponse getCustomer(Long customerId) throws DataNotFoundException {
        Customer customer = customerService.getCustomer(customerId);
        CustomerDtoResponse customerDtoResponse = customerMapper.customerToDtoResponse(customer);
        customerDtoResponse.setEmails(contactService.getAllEmails(customer.getId()));

        return customerDtoResponse;
    }

    public CustomerDtoResponse updateCustomer(CustomerDtoRequest customer, Long customerId) throws DataNotFoundException {
        Customer updatingCustomer = customerMapper.dtoRequestToCustomer(customer);
        Customer updatedCustomer = customerService.updateCustomer(updatingCustomer, customerId);

        List<Contact> contacts = contactService.getAllContacts(customerId);
        List<String> contactsString = contactService.getAllEmails(customerId);
        List<Contact> removedContacts = new ArrayList<>();

        for (Contact contact:contacts){
            if (!customer.getEmails().contains(contact.getEmail())){
                removedContacts.add(contact);
            }
        }
        contactService.deleteEmails(removedContacts);

        contacts.removeAll(removedContacts);

        if (!customer.getEmails().isEmpty()) {
            for (String contact : customer.getEmails()) {
                if (!contactsString.contains(contact)) {
                    contacts.add(new Contact(contact, updatedCustomer));
                }
            }
        }
        contactService.saveContacts(contacts);

        updatedCustomer.setContact(contacts);

        return customerMapper.customerToDtoResponse(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        contactService.deleteEmails(contactService.getAllContacts(customerId));
        customerService.deleteCustomer(customerId);
    }

    public List<CustomerDtoResponse> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        List<CustomerDtoResponse> customerDtoResponseList = new ArrayList<>();
        customerList.forEach(user -> customerDtoResponseList.add(customerMapper.customerToDtoResponse(user)));
        return customerDtoResponseList;
    }
}
