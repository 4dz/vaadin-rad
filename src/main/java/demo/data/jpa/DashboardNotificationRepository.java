package demo.data.jpa;

import demo.domain.DashboardNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardNotificationRepository extends JpaRepository<DashboardNotification, Long> {}
