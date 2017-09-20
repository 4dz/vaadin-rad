package demo.data.jpa;

import demo.data.jpa.DashboardNotificationRepository;
import demo.domain.DashboardNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DashboardNotificationRepositoryIT {

    @Autowired
    private DashboardNotificationRepository dashboardNotificationRepository;

    @Test
    public void shouldReadNotificationsFromDatabase_WhenDatabaseHasNotification() {
        DashboardNotification notification = dashboardNotificationRepository.findOne(1L);
        assertThat(notification.getFirstName(), equalTo("Joe"));
    }

    @Test
    public void shouldAddNotificationsToDatabase() {
        DashboardNotification notification = new DashboardNotification();
        notification.setAction("Read a book");

        dashboardNotificationRepository.save(notification);
        assertThat(notification.getId(), equalTo(3L));
    }
}