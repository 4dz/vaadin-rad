package demo.domain;

import lombok.Data;

@Data
public final class DashboardNotification {
    private long id;
    private String content;
    private boolean read;
    private String firstName;
    private String lastName;
    private String prettyTime;
    private String action;
}