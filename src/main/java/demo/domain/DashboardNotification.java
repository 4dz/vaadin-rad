package demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public final class DashboardNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NonNull String content = "";
    boolean read = false;
    @NonNull String firstName = "";
    @NonNull String lastName = "";
    @NonNull String prettyTime = "";
    @NonNull String action = "";
}