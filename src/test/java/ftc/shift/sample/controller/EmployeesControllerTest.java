package ftc.shift.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.dto.LicenceDtoResponse;
import ftc.shift.sample.service.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeesController.class)
class EmployeesControllerTest {

    private static final String USER_LIST_URL = "/employee/user/list";
    private static final String USER_COUNT_URL = "/employee/user/count";
    private static final String LICENCE_LIST_URL = "/employee/license/list";
    private static final String LICENCE_COUNT_URL = "/employee/license/count";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService service;

    private static List<CustomerDtoResponse> customers;
    private static List<LicenceDtoResponse> licences;

    @BeforeAll
    public static void setup() {

        CustomerDtoResponse customer1 = new CustomerDtoResponse();
        customer1.setId(1L);
        CustomerDtoResponse customer2 = new CustomerDtoResponse();
        customer2.setId(2L);

        LicenceDtoResponse licence1 = new LicenceDtoResponse();
        licence1.setCustomer(customer1);
        LicenceDtoResponse licence2 = new LicenceDtoResponse();
        licence2.setCustomer(customer2);

        customers = List.of(customer1, customer2);
        licences = List.of(licence1, licence2);
    }

    @Test
    void getAllUsers() throws Exception {
        when(service.getAllCustomersWithFilter(any())).thenReturn(customers);

        mockMvc.perform(post(USER_LIST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void getUsersCount() throws Exception {
        when(service.getCustomersCountWithFilter(any())).thenReturn((long) customers.size());

        mockMvc.perform(post(USER_COUNT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customers.size())));
    }

    @Test
    void getAllLicences() throws Exception {
        when(service.getAllLicencesWithFilter(any())).thenReturn(licences);

        mockMvc.perform(post(LICENCE_LIST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(licences)));
    }

    @Test
    void getLicencesCount() throws Exception {
        when(service.getLicencesCountWithFilter(any())).thenReturn((long) licences.size());

        mockMvc.perform(post(LICENCE_COUNT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(licences.size())));
    }
}