package demo.data.jpa;

import demo.domain.CinemaBooking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CinemaBookingRepositoryIT {

    @Autowired
    CinemaBookingRepository repository;

    @Autowired
    MovieRepository movieRepository;

    @Test
    public void shouldAddBookingToDatabase() {
        CinemaBooking booking = new CinemaBooking();
        booking.setFirstName("Joe");
        booking.setLastName("Bloggs");
        booking.setMovie(movieRepository.getOne(1L));
        booking.setNumberSeats(5);
        booking.setShowingDate(Timestamp.valueOf(LocalDateTime.of(2017,11,20,13,55)));

        repository.save(booking);

        CinemaBooking retrieved = repository.findOne(booking.getId());
        assertThat(retrieved, equalTo(booking));
    }
}