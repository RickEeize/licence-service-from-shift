package ftc.shift.sample.service;

import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.dto.LicenceDtoResponse;
import ftc.shift.sample.entity.Contact;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.filter.SearchCriteria;
import ftc.shift.sample.filter.SpecificationBuilder;
import ftc.shift.sample.mapper.CustomerMapper;
import ftc.shift.sample.mapper.LicenceMapper;
import ftc.shift.sample.repository.ContactRepository;
import ftc.shift.sample.repository.CustomerRepository;
import ftc.shift.sample.repository.LicenceRepository;
import org.eclipse.jetty.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final LicenceRepository licenceRepository;
    private final LicenceMapper licenceMapper;
    private final ContactRepository contactRepository;

    @Autowired
    public EmployeeService(CustomerRepository customerRepository,
                           LicenceRepository licenceRepository,
                           CustomerMapper customerMapper,
                           LicenceMapper licenceMapper,
                           ContactRepository contactRepository) {
        this.customerRepository = customerRepository;
        this.licenceRepository = licenceRepository;
        this.customerMapper = customerMapper;
        this.licenceMapper = licenceMapper;
        this.contactRepository = contactRepository;
    }

    public List<CustomerDtoResponse> getAllCustomersWithFilter(List<SearchCriteria> filter) {
        Specification<Customer> spec = this.getSpecifications(filter, Customer.class);
        List<Customer> users = spec == null ? customerRepository.findAll() : customerRepository.findAll(spec);
        for (Customer c: users) {
            List<Contact> emails = contactRepository.findAllByCustomerId(c.getId());
            c.setContact(new ArrayList<>(emails));
        }
        return users.stream().map(customerMapper::customerToDtoResponse).collect(Collectors.toList());
    }

    public Long getCustomersCountWithFilter(List<SearchCriteria> filter) {
        Specification<Customer> spec = this.getSpecifications(filter, Customer.class);
        return spec == null ? customerRepository.count() : customerRepository.count(spec);
    }

    public List<LicenceDtoResponse> getAllLicencesWithFilter(List<SearchCriteria> filter) {
        Specification<Licence> spec = this.getSpecifications(filter, Licence.class);
        List<Licence> licences = spec == null ? licenceRepository.findAll() : licenceRepository.findAll(spec);
        return licences.stream().map(licenceMapper::licenceToDtoResponse).collect(Collectors.toList());
    }

    public Long getLicencesCountWithFilter(List<SearchCriteria> filter) {
        Specification<Licence> spec = this.getSpecifications(filter, Licence.class);
        return spec == null ? licenceRepository.count() : licenceRepository.count(spec);
    }

    private <T> Specification<T> getSpecifications(List<SearchCriteria> filter, Class<T> cl) {
        filter = this.filterCriteriaList(filter, cl);

        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
        for (SearchCriteria criteria : filter) {
            builder.with(criteria);
        }

        return builder.build();
    }

    private List<SearchCriteria> filterCriteriaList(List<SearchCriteria> criteriaList, Class<?> cl) {
        Field[] fields = ArrayUtil.add(cl.getDeclaredFields(), cl.getSuperclass().getDeclaredFields());

        return criteriaList.stream().filter(criteria ->
                Arrays.stream(fields)
                        .map(Field::getName)
                        .anyMatch(fieldName -> fieldName.equals(criteria.getKey()))
        ).collect(Collectors.toList());
    }
}