package com.omidmk.iamapi.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private Long balance;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public UserModel(String email, boolean isAdmin, String firstName, String lastName) {
        this(email, isAdmin, firstName, lastName, 100L);
    }

    public UserModel(String email, boolean isAdmin, String firstName, String lastName, Long balance) {
        this.email = email;
        this.isAdmin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }
}

