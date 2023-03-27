package com.ty_yak.reports.model.dto.reports;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActiveUserReportDto {

    String username;
    String email;
    String city;
    Integer messagesSent;
}
