package demo.data;

import demo.data.jpa.DashboardNotificationRepository;
import demo.data.jpa.MovieRepository;
import demo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service("dataProvider")
public class JpaDataProvider implements DataProvider {
    @Autowired
    private DashboardNotificationRepository notificationRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Collection<Transaction> getRecentTransactions(int count) {
        return Arrays.asList(
                new Transaction()
        );
    }

    @Override
    public Collection<MovieRevenue> getTotalMovieRevenues() {
        return Arrays.asList(
                new MovieRevenue()
        );
    }

    @Override
    public long getUnreadNotificationsCount() {
        movieRepository.findAll();
        return notificationRepository.count();
    }

    @Override
    public Collection<DashboardNotification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public double getTotalSum() {
        return 0;
    }

    @Override
    public Collection<Transaction> getTransactionsBetween(Date startDate, Date endDate) {
        return Arrays.asList(
                new Transaction()
        );
    }
}
