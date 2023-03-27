package com.ty_yak.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_statuses")
@Accessors(chain = true)
@FieldDefaults(level = PRIVATE)
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_statuses_id_seq")
    @SequenceGenerator(name = "users_statuses_id_seq",
            sequenceName = "users_statuses_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    Status status;

    String message;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
