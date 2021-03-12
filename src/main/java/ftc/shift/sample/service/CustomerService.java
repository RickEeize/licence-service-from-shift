package ftc.shift.sample.service;

import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Добавление нового пользователя
     *
     * @param customer - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Сохранённый пользователь
     */
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Получение пользователя с указанным идентификатором
     *
     * @param customerId - Идентификатор пользователя
     */
    public Customer getCustomer(Long customerId) throws DataNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(DataNotFoundException::new);
    }

    /**
     * Добавление нового пользователя
     *
     * @param userId      - Идентификатор пользователя
     * @param updatedCustomer - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Обновленный пользователь
     */
    public Customer updateCustomer(Customer updatedCustomer, Long userId) throws DataNotFoundException {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("USER_NOT_FOUND"));
        customer.setName(updatedCustomer.getName());
        customer.setType(updatedCustomer.getType());
        customer.setRegistrationDate(updatedCustomer.getRegistrationDate());
        return customerRepository.save(customer);
    }

    /**
     * Удаление существующего пользователя
     *
     * @param userId - Идентификатор пользователя, которого необходимо удалить
     */
    public void deleteCustomer(Long userId) {
        customerRepository.delete(customerRepository.getOne(userId));
    }

    /**
     * Получение всех пользователей
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
