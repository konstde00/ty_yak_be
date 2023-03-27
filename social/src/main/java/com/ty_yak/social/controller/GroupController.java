package com.ty_yak.social.controller;

import com.ty_yak.social.model.dto.UpdateGroupDto;
import com.ty_yak.social.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {

    GroupService groupService;

    @PutMapping("/v1")
    @Operation(summary = "Update group")
    public ResponseEntity<?> updateGroup(@RequestBody UpdateGroupDto updateGroupDto) {

        log.info("Update group with params - {}", updateGroupDto);

        groupService.updateGroup(updateGroupDto);

        log.info("Group has been updated successfully");

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
