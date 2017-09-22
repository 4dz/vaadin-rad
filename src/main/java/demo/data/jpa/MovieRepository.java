package demo.data.jpa;

import demo.domain.Movie;
import demo.domain.MovieRevenue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "select new demo.domain.MovieRevenue(min(t.time), t.movie, sum(t.price)) from Transaction t group by t.movie order by sum(t.price) desc")
    List<MovieRevenue> findMovieRevenues(Pageable pageable);

    @Query(value = "select new demo.domain.MovieRevenue(cast(t.time as date), t.movie, sum(t.price)) from Transaction t group by t.movie.id, cast(t.time as date) order by t.movie.id, cast(t.time as date)")
    List<MovieRevenue> findDailyMovieRevenues();

    @Query(value = "select new demo.domain.MovieRevenue(cast(t.time as date), t.movie, sum(t.price)) from Transaction t where t.movie.id = :movieId group by cast(t.time as date) order by cast(t.time as date)")
    List<MovieRevenue> findMovieRevenue(@Param("movieId") long movieId);
}