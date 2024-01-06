package com.omidmk.iamapi.model.ticket;

import com.omidmk.iamapi.model.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dialogs")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class DialogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserModel user;

    @Column(updatable = false)
    private String text;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    public DialogModel(UserModel user, String text) {
        this.user = user;
        this.text = text;
    }
}
