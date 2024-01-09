package com.omidmk.iamapi.model.deployment;

import com.omidmk.iamapi.model.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deployment")
@EntityListeners(AuditingEntityListener.class)
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
    private State state;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public DeploymentModel(String realmName, PlanDV plan) {
        this.realmName = realmName;
        this.plan = plan;
    }

    public enum State {
        DEPLOYING,
        FAILED_TO_DEPLOY,
        DEPLOYED,
        STOPPED
    }
}
