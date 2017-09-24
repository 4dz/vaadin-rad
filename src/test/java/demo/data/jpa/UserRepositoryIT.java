package demo.data.jpa;

import demo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository repository;

    @Test
    public void shouldReadUserFromDatabase() {
        User user = repository.findOne("admin");
        assertThat(user.getFirstName(), equalTo("Adam"));
    }
}