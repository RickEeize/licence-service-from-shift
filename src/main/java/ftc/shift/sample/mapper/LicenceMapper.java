package ftc.shift.sample.mapper;

import ftc.shift.sample.dto.LicenceDtoResponse;
import ftc.shift.sample.entity.Licence;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = CustomerMapper.class)
public interface LicenceMapper {

    LicenceDtoResponse licenceToDtoResponse(Licence licence);
}
