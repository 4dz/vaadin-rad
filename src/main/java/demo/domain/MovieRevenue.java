package demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public final class MovieRevenue {
    private Date timestamp;
    private String title;
    private Double revenue;
}