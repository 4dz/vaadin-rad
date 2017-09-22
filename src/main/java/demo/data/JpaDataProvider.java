package demo.data;

import demo.data.jpa.DashboardNotificationRepository;
import demo.data.jpa.MovieRepository;
import demo.data.jpa.TransactionRepository;
import demo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service("dataProvider")
public class JpaDataProvider implements DataProvider {
    @Autowired
    private DashboardNotificationRepository notificationRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getRecentTransactions(int count) {
        return transactionRepository.findAll(new PageRequest(0, count, new Sort(Sort.Direction.DESC, "time"))).getContent();
    }

    @Override
    public List<MovieRevenue> getTotalMovieRevenues(int count) {
        return movieRepository.findMovieRevenues(new PageRequest(0,count));
    }

    @Override
    public long getUnreadNotificationsCount() {
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
