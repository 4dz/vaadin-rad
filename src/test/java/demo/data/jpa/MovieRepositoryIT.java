package demo.data.jpa;

import demo.data.jpa.MovieRepository;
import demo.domain.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieRepositoryIT {

    @Autowired
    private MovieRepository repository;

    @Test
    public void shouldReadMovieFromDatabase_WhenDatabaseHasMovies() {
        Movie movie = repository.findOne(1L);
        assertThat(movie.getTitle(), equalTo("San Andreas"));
    }

}