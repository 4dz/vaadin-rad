package demo.data;

import demo.data.jpa.UserRepository;
import demo.domain.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JpaDataProviderTest {

    @Test
    public void shouldCheckPasswordWhenAuthenticatingUser() {
        final User u = new User();
        u.setUserName("username");
        u.setHashedPassword("ChJzErIAKCp3szkwE0RGn2tVziBv+QjP8GX/a5Rcgh2vxPB72FfmIiMfm3kCHn5SwNGhLX9t9FzBjLzE8LJBmg==");

        JpaDataProvider provider = new JpaDataProvider();
        provider.userRepository=mock(UserRepository.class);

        when(provider.userRepository.findOne("username")).thenReturn(u);

        User user = provider.authenticate("username", "password");
        assertThat(user, not(nullValue()));
    }
}