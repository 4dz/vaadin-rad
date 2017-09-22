package demo.data.jpa;

import demo.domain.Transaction;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    public void shouldReadRecentTransactionsFromDatabase() {
        List<Transaction> recent = repository.findAll(new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "time"))).getContent();
        assertThat(recent, hasSize(20));
    }

}