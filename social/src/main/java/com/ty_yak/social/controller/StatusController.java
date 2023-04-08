package com.ty_yak.social.controller;

import com.ty_yak.auth.model.enums.StatusEnum;
import com.ty_yak.auth.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statuses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {

    StatusService statusService;

    @PatchMapping("/v1")
    @Operation(summary = "Update status")
    public ResponseEntity<?> updateStatus(@AuthenticationPrincipal Long userId,
                                          @RequestParam StatusEnum status) {

        statusService.updateStatus(userId, status);

        return ResponseEntity.ok().build();
    }
}
