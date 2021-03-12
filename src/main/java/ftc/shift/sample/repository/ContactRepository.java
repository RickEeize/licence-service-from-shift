package ftc.shift.sample.repository;

import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByCustomerId(Long customerId);
}
