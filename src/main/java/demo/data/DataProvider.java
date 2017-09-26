package demo.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import demo.domain.*;

/**
 * QuickTickets Dashboard backend API.
 */
public interface DataProvider {
    /**
     * @param count
     *            Number of transactions to fetch.
     * @return A Collection of most recent transactions.
     */
    List<Transaction> getRecentTransactions(int count);

    /**
     * @return Total revenues for each listed movie.
     */
    List<MovieRevenue> getTotalMovieRevenues(int count);

    /**
     * @return The number of unread notifications for the current user.
     */
    long getUnreadNotificationsCount();

    /**
     * @return Notifications for the current user.
     */
    Collection<DashboardNotification> getNotifications();

    /**
     * @return The total summed up revenue of sold movie tickets
     */
    double getTotalSum();

    Collection<Movie> getMovies();

    void save(CinemaBooking booking);
}