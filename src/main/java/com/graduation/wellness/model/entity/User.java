package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import java.util.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "profilePicture")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column( nullable = false , unique = true )
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    // Fields for OAuth2
    @Column(nullable = false)
    private String provider; // e.g., "google", "facebook"

    @Column( unique = true )
    private String providerUserId;  // Unique ID provided by the provider (e.g., Google ID)

    private byte[] profilePicture;  // ✅ Store file path instead of URL

    @Size(min = 8 , message = "password minLength is 8")
    @Column(nullable = true)
    private String password;

    @Transient
    private GrantedAuthority authorities ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role ;

    public User(String firstName  , String lastName ,
                 String email , String password , String provider ) {
        super();
        this.firstName= firstName;
        this.lastName= lastName;
        this.email = email;
        this.password= password;
        this.provider = provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName())); // Example: ROLE_USER
    }
    @Override
    public String getUsername() {
        return firstName + " " +lastName;
    }
    @Override
    public String getPassword() {
        return password;
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
