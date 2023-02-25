package com.ty_yak.auth.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_tokens")
@FieldDefaults(level = PRIVATE)
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_tokens_id_seq")
    @SequenceGenerator(name = "device_tokens_id_seq",
            sequenceName = "device_tokens_id_seq", allocationSize = 1)
    Long id;

    @Column(name = "device_token")
    String deviceToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;
}
