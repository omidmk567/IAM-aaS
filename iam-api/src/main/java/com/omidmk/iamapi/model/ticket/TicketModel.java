package com.omidmk.iamapi.model.ticket;

import com.omidmk.iamapi.model.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class TicketModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserModel customer;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DialogModel> dialogs;

    @Enumerated(EnumType.STRING)
    private State state;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastModifiedAt;

    @Version
    private Long version;

    public enum State {
        WAITING_FOR_ADMIN_RESPONSE,
        WAITING_FOR_CUSTOMER_RESPONSE,
        CLOSED,
    }
}
