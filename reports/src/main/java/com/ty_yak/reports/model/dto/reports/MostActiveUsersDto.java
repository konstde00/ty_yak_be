package com.ty_yak.reports.model.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MostActiveUsersDto extends ReportResponseDto {

    List<ActiveUserReportDto> activeUsers;
}


