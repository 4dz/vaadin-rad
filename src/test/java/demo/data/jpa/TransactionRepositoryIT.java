package demo.data.jpa;

import demo.domain.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryIT {
    @Autowired
    TransactionRepository repository;

    @Test
    public void shouldReadTransactionFromDatabase_WhenDatabaseHasTransaction() {
        Transaction transaction = repository.findOne(1L);
        assertThat(transaction.getSeats(), equalTo(3));
    }

}