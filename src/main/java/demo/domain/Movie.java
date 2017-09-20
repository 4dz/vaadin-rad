package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @NonNull String title = "";
    @NonNull String synopsis = "";
    @NonNull String thumbUrl = "";
    @NonNull String posterUrl = "";
    int duration;
    @NonNull Date releaseDate = new Date();
    int score;
}
