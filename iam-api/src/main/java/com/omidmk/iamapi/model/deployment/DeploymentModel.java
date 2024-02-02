package com.omidmk.iamapi.model.deployment;

import com.omidmk.iamapi.model.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deployments")
@Data
@NoArgsConstructor
public class DeploymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserModel user;

    @Column(nullable = false, updatable = false, unique = true)
    private String realmName;

    @Enumerated(EnumType.STRING)
    private PlanDV plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public DeploymentModel(UserModel user, String realmName, PlanDV plan, State state) {
        this.user = user;
        this.realmName = realmName;
        this.plan = plan;
        this.state = state;
    }

    public enum State {
        DEPLOYING,
        FAILED_TO_DEPLOY,
        DEPLOYED,
        STOPPED,
    }
}
