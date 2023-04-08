package com.ty_yak.auth.service;

import com.ty_yak.auth.model.entity.Status;
import com.ty_yak.auth.model.entity.UserStatus;
import com.ty_yak.auth.model.enums.StatusEnum;
import com.ty_yak.auth.repository.StatusRepository;
import com.ty_yak.auth.repository.UserStatusRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusService {

    UserService userService;
    StatusRepository statusRepository;
    UserStatusRepository userStatusRepository;

    public StatusEnum getStatusByUserId(Long userId) {

        var status = statusRepository.getStatusByUserId(userId);

        return status == null ? StatusEnum.OK : StatusEnum.valueOf(status);
    }

    public void updateStatus(Long userId, StatusEnum statusEnum) {

        var user = userService.getById(userId);

        var status = UserStatus.builder()
                .user(user)
                .status(new Status(statusEnum.getId()))
                .createdAt(LocalDateTime.now())
                .build();

        userStatusRepository.saveAndFlush(status);
    }
}
