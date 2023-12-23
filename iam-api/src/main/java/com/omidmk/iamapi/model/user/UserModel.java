package com.omidmk.iamapi.model.user;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
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

    @OneToMany
    private List<DeploymentModel> deployments;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public UserModel(String email, String firstName, String lastName) {
        this(email, firstName, lastName, 0L);
    }

    public UserModel(String email, String firstName, String lastName, Long balance) {
        this(email, firstName, lastName,  balance, List.of());
    }

    public UserModel(String email, String firstName, String lastName, Long balance, List<DeploymentModel> deployments) {
        this.email = email;
        this.balance = balance;
        this.deployments = deployments;
    }
}

