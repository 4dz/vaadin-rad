package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public final class DashboardNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String content;
    private boolean read;
    private String firstName;
    private String lastName;
    private String prettyTime;
    private String action;
}