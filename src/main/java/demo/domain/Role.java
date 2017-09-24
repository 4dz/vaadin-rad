package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    @NonNull
    @Id
    String name;
}
