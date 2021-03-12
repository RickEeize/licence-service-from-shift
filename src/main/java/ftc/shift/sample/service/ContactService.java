package ftc.shift.sample.service;

import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void addEmails(List<String> emails, Customer customer) {
        List<Contact> contacts = new ArrayList<>();
        emails.forEach((e) -> contacts.add(new Contact(e, customer)));
        contactRepository.saveAll(contacts);
    }

    public void saveContacts(List<Contact> contacts) {
        contactRepository.saveAll(contacts);
    }

    public void deleteEmails(List<Contact> emails){
        contactRepository.deleteAll(emails);
    }

    public List<Contact> getAllContacts(Long customerId) {
        return contactRepository.findAllByCustomerId(customerId);
    }

    public List<String> getAllEmails(Long customerId) {
        List<String> emails = new ArrayList<>();

        List<Contact> contacts = contactRepository.findAllByCustomerId(customerId);
        if (!contacts.isEmpty()) {
            for (Contact contact : contacts) {
                emails.add(contact.getEmail());
            }
        }
        return emails;
    }

}
