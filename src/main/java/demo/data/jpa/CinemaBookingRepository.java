package demo.data.jpa;

import demo.domain.CinemaBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaBookingRepository extends JpaRepository<CinemaBooking, Long> {}

