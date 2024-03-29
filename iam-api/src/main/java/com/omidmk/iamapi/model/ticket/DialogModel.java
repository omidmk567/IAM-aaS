package com.omidmk.iamapi.model.ticket;

import com.omidmk.iamapi.model.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dialogs")
@Data
@NoArgsConstructor
public class DialogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private TicketModel ticket;

    @Column(updatable = false, nullable = false)
    private String text;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public DialogModel(UserModel user, String text) {
        this.user = user;
        this.text = text;
    }
}
