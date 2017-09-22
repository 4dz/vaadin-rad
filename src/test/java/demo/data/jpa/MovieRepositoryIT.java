package demo.data.jpa;

import demo.domain.Movie;
import demo.domain.MovieRevenue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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

    @Test
    @Repeat(3)
    public void shouldReadMovieRevenuesFromDatabase_WhenDatabaseHasMovies() {
        List<MovieRevenue> movies = repository.findDailyMovieRevenues();
        assertThat(movies.size(), equalTo(9));
        assertThat(movies.get(0).getRevenue(), equalTo(126.65));
    }

    @Test
    @Repeat(3)
    public void shouldReadMovieRevenueFromDatabase_WhenDatabaseHasMovies() {
        List<MovieRevenue> movies = repository.findMovieRevenue(2);
        assertThat(movies.size(), equalTo(3));
        assertThat(movies.get(0).getRevenue(), equalTo(149.25D));
    }

    @Test
    public void shouldReadTopMoviesFromDatabase_WhenDatabaseHasMovies() {
        List<MovieRevenue> movies = repository.findMovieRevenues(new PageRequest(0,10));
        assertThat(movies.size(), equalTo(3));

        StringBuilder builder = new StringBuilder();
        movies.forEach(entry -> builder.append(String.format("%s %f\n", entry.getMovie().getTitle(), entry.getRevenue())));
        System.err.println(builder);
    }

}