package com.ty_yak.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
@FieldDefaults(level = PRIVATE)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_tokens_id_seq")
    @SequenceGenerator(name = "refresh_tokens_id_seq",
            sequenceName = "refresh_tokens_id_seq", allocationSize = 1)
    Long id;

    String token;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @CreationTimestamp
    LocalDateTime createdAt;

    LocalDateTime expiredAt;
}
