package demo.data;

import demo.domain.DashboardNotification;
import demo.domain.MovieRevenue;
import demo.domain.Transaction;
import demo.domain.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service("dataProvider")
public class DummyDataProvider implements DataProvider {
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
    public User authenticate(String userName, String password) {
        User user = new User();
        user.setEmail(userName);
        return user;
    }

    @Override
    public int getUnreadNotificationsCount() {
        return 1;
    }

    @Override
    public Collection<DashboardNotification> getNotifications() {
        return Arrays.asList(
                new DashboardNotification()
        );
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
