package demo.domain;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
public final class MovieRevenue {

    private Date timestamp;
    private Movie movie;
    private Double revenue;
}