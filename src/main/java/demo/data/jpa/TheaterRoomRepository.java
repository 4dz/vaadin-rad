package demo.data.jpa;

import demo.domain.TheaterRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRoomRepository extends JpaRepository<TheaterRoom, Long> {}
