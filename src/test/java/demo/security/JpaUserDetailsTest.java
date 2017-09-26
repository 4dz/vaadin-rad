package demo.security;

import demo.domain.Role;
import demo.domain.User;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class JpaUserDetailsTest {

    @Test
    public void shouldIndicateRoleAuthority_WhenReturningUserRoles() {
        User orgUser = new User();
        orgUser.setRoles(Collections.singletonList(new Role("ADMIN")));
        JpaUserDetails user = new JpaUserDetails(orgUser);

        assertThat(user.getAuthorities().iterator().next().getAuthority(), equalTo("ROLE_ADMIN"));
    }

}