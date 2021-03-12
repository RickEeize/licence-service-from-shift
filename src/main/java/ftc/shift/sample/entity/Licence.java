package ftc.shift.sample.entity;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "licences")
public class Licence {


    /*
     * Сериализатору нужно обязательно говорить учитывать аннотации
     * Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
     */

    @Id
    @Expose//Анотация говорит gson сериализовать это поле
    private UUID id;

    @NonNull
    @Column(length = 8000)
    private String privateKey;

    @NonNull
    @Expose
    @Column(length = 8000)
    private String licenceKey;

    @NonNull
    @Expose
    private Date createDate;

    @NonNull
    @Expose
    private Date endDate;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NonNull
    @Expose
    private String type;

    @Nullable
    @Expose
    private Long numberOfLicences;

    @Nullable
    @Expose
    private String productType;

    @Nullable
    @Expose
    private String productVersion;
}
