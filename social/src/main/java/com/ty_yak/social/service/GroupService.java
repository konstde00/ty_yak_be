package com.ty_yak.social.service;

import com.ty_yak.auth.exception.AlreadyExistException;
import com.ty_yak.auth.exception.UserNotInGroupException;
import com.ty_yak.social.model.dto.UpdateGroupDto;
import com.ty_yak.social.repository.GroupRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupService {

    GroupRepository groupRepository;

    public List<String> getEmailsByUserId(Long userId) {
        return Objects.requireNonNullElse(groupRepository
                .getEmailsByUserId(userId), Collections.emptyList());
    }

    public void updateGroup(UpdateGroupDto updateGroupDto) {

        if (updateGroupDto.getNewName() != null) {
            groupRepository.updateName(updateGroupDto.getId(), updateGroupDto.getNewName());
        }

        if (updateGroupDto.getUserToAdd() != null) {

            checkIfUserAlreadyInGroup(updateGroupDto.getId(), updateGroupDto.getUserToAdd());
            groupRepository.addUser(updateGroupDto.getId(), updateGroupDto.getUserToAdd());
        }

        if (updateGroupDto.getUserToRemove() != null) {

            checkIfUserNotInGroup(updateGroupDto.getId(), updateGroupDto.getUserToRemove());
            groupRepository.removeUserFromGroup(updateGroupDto.getId(), updateGroupDto.getUserToRemove());
        }
    }

    private void checkIfUserAlreadyInGroup(Long groupId, Long userId) {

        if (groupRepository.isUserInGroup(groupId, userId)) {
            throw new AlreadyExistException("User already in group");
        }
    }

    private void checkIfUserNotInGroup(Long groupId, Long userId) {

        if (!groupRepository.isUserInGroup(groupId, userId)) {
            throw new UserNotInGroupException("User not in group");
        }
    }
}
