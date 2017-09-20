package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public final class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NonNull String country = "";
    @NonNull String city = "";
    @NonNull String name = "";
}
