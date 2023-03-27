package com.ty_yak.reports.mapper;

import com.ty_yak.reports.model.dto.exports.MostActiveUsersRow;
import com.ty_yak.reports.model.dto.reports.ActiveUserReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {

    public MostActiveUsersRow toRow(ActiveUserReportDto dto) {

        return new MostActiveUsersRow(
                dto.getUsername(),
                dto.getEmail(),
                dto.getCity(),
                dto.getMessagesSent());
    }

    public List<MostActiveUsersRow> toRowList(List<ActiveUserReportDto> dtos) {

        return dtos.stream()
                .map(this::toRow)
                .toList();
    }

    public ActiveUserReportDto toDto(MostActiveUsersRow row) {

        return new ActiveUserReportDto(
                row.getUsername(),
                row.getEmail(),
                row.getCity(),
                row.getMessagesSent());
    }

    public List<ActiveUserReportDto> toDtoList(List<MostActiveUsersRow> rows) {

        return rows.stream()
                .map(this::toDto)
                .toList();
    }
}
