package demo.security;

import demo.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class JpaUserDetails extends User implements UserDetails {

    public JpaUserDetails(User user) {
        super(user.getUserName(), user.getHashedPassword(), user.getRoles(), user.getFirstName(), user.getLastName(),
                user.getTitle(), user.isMale(), user.getEmail(), user.getLocation(), user.getPhone(),
                user.getNewsletterSubscription(), user.getWebsite(), user.getBio());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles= StringUtils.collectionToCommaDelimitedString(getRoles());
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    @Override
    public String getPassword() {
        return getHashedPassword();
    }

    @Override
    public String getUsername() {
        return getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
