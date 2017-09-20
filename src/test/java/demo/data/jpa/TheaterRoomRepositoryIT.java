package demo.data.jpa;

import demo.domain.Theater;
import demo.domain.TheaterRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TheaterRoomRepositoryIT {
    @Autowired
    TheaterRoomRepository repository;

    @Test
    public void shouldReadTheatreFromDatabase_WhenDatabaseHasTheatres() {
        TheaterRoom room = repository.findOne(1L);
        assertThat(room.getRoomName(), equalTo("Room 1"));
        assertThat(room.getTheater().getCity(), equalTo("Shanghai"));
    }

}