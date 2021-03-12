package ftc.shift.sample.mapper;

import ftc.shift.sample.entity.Contact;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public class ContactMapper {

    String map(Contact contact){
        return contact.getEmail();
    }
}