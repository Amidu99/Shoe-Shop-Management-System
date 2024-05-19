package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
@Entity
public class User implements UserDetails, SuperEntity {
    @Id
    private String userCode;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "employeeCode")
    private Employee employee;

    @OneToMany(mappedBy = "user")
    private Set<Sale> sales = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
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