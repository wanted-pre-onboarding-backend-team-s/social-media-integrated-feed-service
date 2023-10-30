package com.wanted.feed.user.domain;

import com.wanted.feed.user.exception.ApprovedUserException;
import com.wanted.feed.user.exception.MismatchPasswordException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean approved = false;

    @Builder
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void approveUser() {
        this.approved = true;
    }

    public void checkApproval() {
        if (this.isApproved()) {
            throw new ApprovedUserException();
        }
    }

    public void checkPasswordMatches(String password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, this.getPassword())) {
            throw new MismatchPasswordException();
        }
    }
}
