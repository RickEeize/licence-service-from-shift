package ftc.shift.sample.facade;

import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.util.Constants;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.service.LicenceService;
import ftc.shift.sample.service.CustomerService;
import ftc.shift.sample.util.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ftc.shift.sample.util.Constants.*;

@Service
public class LicenceFacade {

    private final LicenceService licenceService;
    private final CustomerService customerService;

    @Autowired
    public LicenceFacade(LicenceService licenceService, CustomerService customerService) {
        this.licenceService = licenceService;
        this.customerService = customerService;
    }

    public String createLicence(Long userId, String type, Integer numberOfProducts, String productType, String productVersion) throws LicenceGeneratorException, BadRequestException, DataNotFoundException {
        Customer customer = customerService.getCustomer(userId);
        Licence licence;

        if (Constants.CUSTOMER_TYPE_USER.equals(customer.getType()) && LICENCE_TYPE_ORDINARY.equals(type)) {
            licence = LicenceUtil.generateLicence(userId, 100L, productType, productVersion);
            licence.setType(LICENCE_TYPE_ORDINARY);
            licence.setNumberOfLicences((long) 1);

        } else if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
            licence = LicenceUtil.generateLicence(userId, 100L, productType, productVersion);
            licence.setType(type);
            licence.setNumberOfLicences((long) (LICENCE_TYPE_MULTI.equals(type) ? numberOfProducts : 1));

        } else {
            throw new BadRequestException("BAD_REQUEST");
        }

        return LicenceUtil.generateLicenseString(licenceService.createLicence(licence));
    }

    public List<String> createManyLicences(Long userId, Integer count, String productType, String productVersion) throws DataNotFoundException, LicenceGeneratorException, BadRequestException {
        List<String> licences = new ArrayList<>();

        Customer customer = customerService.getCustomer(userId);
        if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
            for (int i = 0; i < count; i++) {
                Licence licence = LicenceUtil.generateLicence(userId, 100L, productType, productVersion);
                licence.setType(LICENCE_TYPE_ORDINARY);
                licence.setNumberOfLicences((long) 1);
                String licenceString = LicenceUtil.generateLicenseString(licenceService.createLicence(licence));
                licences.add(licenceString);
            }
        } else {
            throw new BadRequestException("BAD_REQUEST");
        }
        return licences;
    }
    public List<UUID> getAllCompanyLicencesId(Long id) throws DataNotFoundException, BadRequestException {
        Customer customer = customerService.getCustomer(id);

        if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
            return licenceService.getAllCompanyLicencesId(id);
        } else {
            throw new BadRequestException(USER_IS_NOT_COMPANY);
        }
    }

    public String getLicence(UUID licenceId, Long userId) throws DataNotFoundException {
        Licence licence = licenceService.getLicence(licenceId);
        Customer customer = customerService.getCustomer(userId);

        if (licence.getCustomer().getId().equals(userId)) {
            if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
                licence = licenceService.getLicenceCompany(userId);
                return LicenceUtil.generateLicenseString(licence);
            }
            return getLicence(licenceId);
        } else {
            throw new DataNotFoundException(LICENCE_NOT_EXIST);
        }
    }

    public String getLicence(UUID licenceId) throws DataNotFoundException {
        return LicenceUtil.generateLicenseString(licenceService.getLicence(licenceId));
    }

    public boolean isLicenceCorrect(String licenceString, String productType, String productVersion) throws LicenceException, DataNotFoundException {
        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);
            Licence privateLicence = licenceService.getLicence(licence.getId());

            if (!licence.getProductType().equals(productType)) {
                throw new LicenceException(PRODUCT_TYPE_DOESNT_MATCH);
            }

            if (!licence.getProductVersion().equals(productVersion)) {
                throw new LicenceException(PRODUCT_VERSION_DOESNT_MATCH);
            }

            if (Date.valueOf(LocalDate.now()).before(privateLicence.getEndDate())) {
                String privateKey = privateLicence.getPrivateKey();
                return LicenceUtil.isLicenceCorrect(licence, privateKey);
            } else {
                throw new LicenceException("LICENCE_EXPIRED");
            }
        } catch (LicenceDecodeException e) {
            throw new LicenceException(LICENCE_NOT_EXIST);
        }
    }
}
