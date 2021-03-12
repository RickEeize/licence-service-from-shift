package ftc.shift.sample.controller;

import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.facade.CustomerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomersController {
    private static final String USERS_PATH = "/users";
    private final CustomerFacade customerFacade;

    @Autowired
    public CustomersController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    /**
     * Добавление нового пользователя
     *
     * @param customer - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Сохранённый пользователь
     */
    @PostMapping(USERS_PATH)
    public ResponseEntity<CustomerDtoResponse> createUser(@RequestBody CustomerDtoRequest customer) {
        CustomerDtoResponse result = customerFacade.createCustomer(customer);
        return ResponseEntity.ok(result);
    }

    /**
     * Получение пользователя с указанным идентификатором
     *
     * @param customerId - Идентификатор пользователя
     */
    @GetMapping(USERS_PATH + "/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(customerFacade.getCustomer(customerId));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Обновление существующего пользователя
     *
     * @param customerId - Идентификатор пользователя
     * @param customer   - Новые данные для пользователя (имя, тип, дата регистрации)
     * @return Обновленный пользователь
     */
    @PutMapping(USERS_PATH + "/{customerId}")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDtoRequest customer, @PathVariable Long customerId) {
        try {
            CustomerDtoResponse updatedUser = customerFacade.updateCustomer(customer, customerId);
            return ResponseEntity.ok(updatedUser);
        } catch (DataNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Удаление существующего пользователя
     *
     * @param customerId - Идентификатор пользователя, которого необходимо удалить
     */
    @DeleteMapping(USERS_PATH + "/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        customerFacade.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение всех пользователей
     */
    @GetMapping(USERS_PATH)
    public ResponseEntity<List<CustomerDtoResponse>> getAllCustomers() {
        List<CustomerDtoResponse> customers = customerFacade.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}