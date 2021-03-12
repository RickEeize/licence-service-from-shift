package ftc.shift.sample.controller;

import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.dto.LicenceDtoResponse;
import ftc.shift.sample.filter.SearchCriteria;
import ftc.shift.sample.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class EmployeesController {
    private static final String EMPLOYEES_PATH = "employee";
    private final EmployeeService employeeService;

    @Autowired
    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    /**
     * Получение списка пользователей подходящих под фильтр
     *
     * @param filter - фильтр
     * @return 200 OK, возможно пустой массив json объектов, подходящих под фильтр
     */
    @PostMapping(value = EMPLOYEES_PATH + "/user/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDtoResponse>> getAllUsers(@RequestBody List<SearchCriteria> filter) {
        List<CustomerDtoResponse> customers = employeeService.getAllCustomersWithFilter(filter);
        return ResponseEntity.ok(customers);
    }

    /**
     * Получение количества пользователей, подходящих под фильтр
     *
     * @param filter - фильтр
     * @return 200 OK, количество пользователей, подходящих под фильтр
     */
    @PostMapping(value = EMPLOYEES_PATH + "/user/count")
    public ResponseEntity<Long> getUsersCount(@RequestBody List<SearchCriteria> filter) {
        return ResponseEntity.ok(employeeService.getCustomersCountWithFilter(filter));
    }

    /**
     * Получение списка лицензий, подходящих под фильтр
     *
     * @param filter - фильтр
     * @return 200 OK, возможно пустой массив json объектов, подходящих под фильтр
     */
    @PostMapping(value = EMPLOYEES_PATH + "/license/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LicenceDtoResponse>> getAllLicences(@RequestBody List<SearchCriteria> filter) {
        List<LicenceDtoResponse> licences = employeeService.getAllLicencesWithFilter(filter);
        return ResponseEntity.ok(licences);
    }

    /**
     * Получение количества лицензий, подходящих под фильтр
     *
     * @param filter - фильтр
     * @return 200 OK, количество лицензий, подходящих под фильтр
     */
    @PostMapping(value = EMPLOYEES_PATH + "/license/count")
    public ResponseEntity<Long> getLicencesCount(@RequestBody List<SearchCriteria> filter) {
        return ResponseEntity.ok(employeeService.getLicencesCountWithFilter(filter));
    }
}

