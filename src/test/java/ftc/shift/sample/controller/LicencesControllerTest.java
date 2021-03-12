package ftc.shift.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.facade.LicenceFacade;
import ftc.shift.sample.util.LicenceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static ftc.shift.sample.util.Constants.LICENCE_TYPE_ORDINARY;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LicencesController.class)
class LicencesControllerTest {

    private static final String CREATE_LICENCE_URL = "/licences/new";
    private static final String GET_LICENCE_URL = "/licences/";
    private static final String GET_ALL_COMPANY_LICENCES_ID_URL = "/licences/list";
    private static final String CHECK_LICENCE = "/licences/check";
    private static final String LICENCE_NOT_EXIST = "LICENCE_NOT_EXIST";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LicenceFacade licenceFacade;

    @Test
    void createLicenceForUser() throws Exception {
        Long id = 1L;
        String type = LICENCE_TYPE_ORDINARY;

        Licence licence = LicenceUtil.generateLicence(id, 100L, "1", "1");
        licence.setType(type);
        licence.setNumberOfLicences((long) 1);

        String licenceString = LicenceUtil.generateLicenseString(licence);

        when(licenceFacade.createLicence(id, type, 1, "1", "1")).thenReturn(licenceString);

        mockMvc.perform(post(CREATE_LICENCE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(id))
                .param("type", type)
                .param("numberOfProducts", String.valueOf(1))
                .param("count", String.valueOf(0))
                .param("productType", "1")
                .param("productVersion", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(licenceString));
    }

    @Test
    void getLicence() throws Exception {
        Long id = 1L;
        Licence licence = LicenceUtil.generateLicence(id, 100L, "1", "1");
        UUID licenceId = licence.getId();
        String licenceString = LicenceUtil.generateLicenseString(licence);

        when(licenceFacade.getLicence(licenceId, id)).thenReturn(licenceString);

        mockMvc.perform(post(GET_LICENCE_URL + licenceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(id))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(licenceString));
    }

    @Test
    void getAllCompanyLicencesId() throws Exception {
        Long userId = 1L;
        List<UUID> uuidList = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            uuidList.add(UUID.randomUUID());
        }

        when(licenceFacade.getAllCompanyLicencesId(userId)).thenReturn(uuidList);

        mockMvc.perform(post(GET_ALL_COMPANY_LICENCES_ID_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(userId))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(uuidList)));
    }

    @Test
    void checkLicence() throws Exception {
        Long userId = 1L;
        Licence licence = LicenceUtil.generateLicence(userId, 100L, "1", "1");
        String licenceString = LicenceUtil.generateLicenseString(licence);

        when(licenceFacade.isLicenceCorrect(licenceString, "1", "1")).thenReturn(true);

        mockMvc.perform(post(CHECK_LICENCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(licenceString)
                .param("productType", "1")
                .param("productVersion", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void checkLicenceIfNotExist() throws Exception {
        Long userId = 1L;
        Licence licence = LicenceUtil.generateLicence(userId, 100L, "1", "1");
        String licenceString = LicenceUtil.generateLicenseString(licence);

        when(licenceFacade.isLicenceCorrect(licenceString, "1", "1")).thenReturn(true);

        mockMvc.perform(post(CHECK_LICENCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("BAD LICENCE")
                .param("productType", "1")
                .param("productVersion", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(418))
                .andExpect(content().string(LICENCE_NOT_EXIST));
    }
}