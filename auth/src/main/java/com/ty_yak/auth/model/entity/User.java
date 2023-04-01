package com.ty_yak.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ty_yak.auth.model.enums.CountryCode;
import com.ty_yak.auth.model.enums.Role;
import com.ty_yak.auth.model.enums.UserRegistrationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq",
            sequenceName = "users_id_seq", allocationSize = 1)
    Long id;

    String name;

    String username;

    String password;

    @Column(name = "avatar_url")
    String avatarUrl;

    String email;

    String city;

    @Column(name = "push_notifications_enabled")
    Boolean pushNotificationsEnabled;

    @Enumerated(STRING)
    @Column(name = "registration_type")
    UserRegistrationType registrationType;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "role")
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    Collection<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    Collection<DeviceToken> deviceTokens;

    @OneToMany(mappedBy = "user")
    Collection<ConfirmationCode> confirmationCodes;

    @Column(name = "native_country_code")
    @Enumerated(STRING)
    CountryCode nativeCountryCode;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    LocalDateTime updatedAt;

    public User(String username, String email, String password, String city) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
    }
}
