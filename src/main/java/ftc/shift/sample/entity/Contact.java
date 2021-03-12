package ftc.shift.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contacts")
public class Contact extends Identifiable {

    private String email;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
