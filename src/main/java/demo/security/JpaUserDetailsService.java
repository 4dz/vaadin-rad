package demo.security;

import demo.data.jpa.UserRepository;
import demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findOne(username);
        if(null == user) {
            throw new UsernameNotFoundException("No user present with username: "+username);
        } else {
            return new JpaUserDetails(user);
        }
    }
}