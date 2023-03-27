package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    @Modifying
    @Query("update DeviceToken dt " +
            "set dt.deviceToken = :deviceToken " +
            "where dt.user.id = :userId")
    void updateDeviceToken(@Param("deviceToken") String deviceToken,
                           @Param("userId") Long userId);
}
