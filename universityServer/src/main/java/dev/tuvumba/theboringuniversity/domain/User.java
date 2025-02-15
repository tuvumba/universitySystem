package dev.tuvumba.theboringuniversity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
public class User implements UserDetails {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Id
    private String username;

    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return username;
    }
}
