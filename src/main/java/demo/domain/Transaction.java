package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public final class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NonNull Date time = new Date();

    @ManyToOne
    @JoinColumn(name="theater_room_id")
    @NonNull TheaterRoom room = new TheaterRoom();

    int seats;
    double price;

    @ManyToOne
    @JoinColumn(name="movie_id")
    @NonNull Movie movie = new Movie();
}