package com.ty_yak.social.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateGroupDto {

    Long id;

    String newName;

    Long userToAdd;

    Long userToRemove;
}
