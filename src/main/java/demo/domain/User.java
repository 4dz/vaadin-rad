package demo.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Entity
public final class User {
    @Id
    @NonNull String userName = "";
    @NonNull String hashedPassword = "";


    /** Here is the third table **/
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="user_role", joinColumns = @JoinColumn(name="user_name"),
            inverseJoinColumns = @JoinColumn(name="role_name"))
    @NonNull List<Role> roles = Collections.emptyList();

    @NonNull String firstName = "";
    @NonNull String lastName = "";
    @NonNull String title = "";
    private boolean male;
    @NonNull String email = "";
    @NonNull String location = "";
    @NonNull String phone = "";
    Integer newsletterSubscription;
    String website;
    String bio;
}