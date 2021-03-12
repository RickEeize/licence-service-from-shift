package ftc.shift.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;
import java.util.List;

@Data
@Getter
public class CustomerDtoRequest {

    @ApiModelProperty(value = "Тип пользователя", example = "Физическое лицо")
    private String type;

    @ApiModelProperty(value = "Название компании/Имя пользователя", example = "Иванов Иван Иванович")
    private String name;

    @ApiModelProperty(value = "Дата регистрации", example = "01.03.2021")
    private Date registrationDate;

    @ApiModelProperty(value = "Emails", example = "i.ivanov@gmail.com")
    private List<String> emails;
}
