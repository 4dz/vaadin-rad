package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
public final class TheaterRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    @JoinColumn(name="theater_id")
    @NonNull Theater theater = new Theater();

    @NonNull String roomName = "";
}
