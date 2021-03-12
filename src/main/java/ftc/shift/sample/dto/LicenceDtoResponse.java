package ftc.shift.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class LicenceDtoResponse {

    @ApiModelProperty(value = "Идентификатор лицензии", example = "1")
    private UUID id;

    @ApiModelProperty(value = "Ключ лицензии")
    private String licenceKey;

    @ApiModelProperty(value = "Дата создания лицензии", example = "2021-03-04")
    private Date createDate;

    @ApiModelProperty(value = "Дата окончания лицензии", example = "2021-03-04")
    private Date endDate;

    @ApiModelProperty(value = "Идентификатор пользователя, которому выдана лицензия", example = "1")
    private CustomerDtoResponse customer;

    @ApiModelProperty(value = "Тип лицензии", example = "Мульти")
    private String type;

    @ApiModelProperty(value = "Количество копий лицензии", example = "12")
    private Long numberOfLicences;

    @ApiModelProperty(value = "Тип продукта", example = "Visual Studio")
    private String productType;

    @ApiModelProperty(value = "Версия продукта", example = "1.0.2")
    private String productVersion;
}

