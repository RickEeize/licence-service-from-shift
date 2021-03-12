package ftc.shift.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.facade.CustomerFacade;
import ftc.shift.sample.util.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomersController.class)
class CustomersControllerTest {

    private static final String USERS_PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerFacade customerFacade;

    private static CustomerDtoRequest customerDtoRequest;
    private static CustomerDtoResponse customerDtoResponse;

    @BeforeAll
    public static void setup() {
        customerDtoRequest = new CustomerDtoRequest();
        customerDtoRequest.setName("Max");
        customerDtoRequest.setType(Constants.CUSTOMER_TYPE_COMPANY);
        customerDtoRequest.setRegistrationDate(Date.valueOf(LocalDate.now()));

        customerDtoResponse = new CustomerDtoResponse();
        customerDtoResponse.setId(1L);
        customerDtoResponse.setName("Max");
        customerDtoResponse.setType(Constants.CUSTOMER_TYPE_COMPANY);
        customerDtoResponse.setRegistrationDate(Date.valueOf(LocalDate.now()));
    }

    @Test
    void createUser() throws Exception {
        when(customerFacade.createCustomer(any(CustomerDtoRequest.class))).thenReturn(customerDtoResponse);

        mockMvc.perform(post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDtoRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerDtoResponse)));
    }

    @Test
    void getCustomer() throws Exception {
        when(customerFacade.getCustomer(1L)).thenReturn(customerDtoResponse);

        mockMvc.perform(get(USERS_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerDtoResponse)));
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerFacade.updateCustomer(any(CustomerDtoRequest.class), eq(1L))).thenReturn(customerDtoResponse);

        mockMvc.perform(put(USERS_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDtoRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerDtoResponse)));
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete(USERS_PATH + "/1"))
                .andExpect(status().isOk());

        verify(customerFacade, times(1)).deleteCustomer(1L);
    }

    @Test
    void getAllCustomers() throws Exception {
        List<CustomerDtoResponse> customerDtoResponseList = List.of(customerDtoResponse);
        when(customerFacade.getAllCustomers()).thenReturn(customerDtoResponseList);

        mockMvc.perform(get(USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerDtoResponseList)));
    }
}