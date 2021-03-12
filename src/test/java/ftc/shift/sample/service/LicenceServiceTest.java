package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.repository.LicenceRepository;
import ftc.shift.sample.util.LicenceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static ftc.shift.sample.util.Constants.LICENCE_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class LicenceServiceTest {

    @MockBean
    private LicenceRepository repository;

    private LicenceService licenceService;

    @BeforeEach
    void setUp() {
        licenceService = new LicenceService(repository);
    }

    @Test
    void createLicence() {
        licenceService.createLicence(new Licence());
        verify(repository, times(1)).save(any(Licence.class));
    }

    @Test
    void getLicenceCompany() {
        List<Licence> licenceList = new LinkedList<>();

        licenceList.add(new Licence());

        Licence lastLicence = new Licence();
        licenceList.add(lastLicence);

        when(repository.findLicencesByCustomerId(1L)).thenReturn(licenceList);
        Licence result = licenceService.getLicenceCompany(1L);
        assertEquals(lastLicence, result);
    }

    @Test
    void getLicenceIfNotExist() throws DataNotFoundException {
        Exception exception = assertThrows(DataNotFoundException.class, () ->
                licenceService.getLicence(null));
        assertEquals(LICENCE_NOT_EXIST, exception.getMessage());

        UUID uuid = UUID.randomUUID();
        Licence licence = new Licence();
        when(repository.findById(uuid)).thenReturn(java.util.Optional.of(licence));

        Licence result = licenceService.getLicence(uuid);
        assertEquals(licence, result);
    }

    @Test
    void getLicence() throws DataNotFoundException {
        UUID uuid = UUID.randomUUID();
        Licence licence = new Licence();
        when(repository.findById(uuid)).thenReturn(java.util.Optional.of(licence));

        Licence result = licenceService.getLicence(uuid);
        assertEquals(licence, result);
    }

    @Test
    void getAllCompanyLicencesId() throws LicenceGeneratorException {
        List<Licence> licences = new LinkedList<>();

        Licence firstLicence = LicenceUtil.generateLicence(1L, 100L, "1", "1");
        Licence secondLicence = LicenceUtil.generateLicence(1L, 100L, "1", "1");

        licences.add(firstLicence);
        licences.add(secondLicence);

        when(repository.findLicencesByCustomerId(1L)).thenReturn(licences);
        List<UUID> licenceIdList = licenceService.getAllCompanyLicencesId(1L);
        assertEquals(firstLicence.getId(), licenceIdList.get(0));
        assertEquals(secondLicence.getId(), licenceIdList.get(1));
    }
}