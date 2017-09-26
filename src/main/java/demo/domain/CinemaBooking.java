package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class CinemaBooking {

    @Id long id;

    @NonNull String firstName = "";
    @NonNull String lastName = "";

    @ManyToOne
    @JoinColumn(name="movie_id")
    @NonNull Movie movie = new Movie();

    @NonNull Date showingDate =  new Date();

    int numberSeats;
}
