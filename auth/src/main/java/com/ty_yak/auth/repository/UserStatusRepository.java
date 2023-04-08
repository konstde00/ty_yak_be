package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}
