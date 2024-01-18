package com.omidmk.iamapi.model.user;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private Long balance;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeploymentModel> deployments;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TicketModel> tickets;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public UserModel(String email, String firstName, String lastName) {
        this(email, firstName, lastName, 100L);
    }

    public UserModel(String email, String firstName, String lastName, Long balance) {
        this(email, firstName, lastName,  balance, List.of());
    }

    public UserModel(String email, String firstName, String lastName, Long balance, List<DeploymentModel> deployments) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.deployments = deployments;
        this.tickets = new ArrayList<>();
    }
}

