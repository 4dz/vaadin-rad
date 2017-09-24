package demo.ui.event;

import java.util.Collection;
import demo.domain.Transaction;
import demo.ui.view.DashboardViewType;
import lombok.Data;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    @Data
    public static final class UserLoginRequestedEvent {
        final String userName, password;
        final boolean rememberMe;
    }

    @Data
    public static final class UserLoginFailedEvent {
        final String message;
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    @Data
    public static final class PostViewChangeEvent {
        final DashboardViewType view;
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}