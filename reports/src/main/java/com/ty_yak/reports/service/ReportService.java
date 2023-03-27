package com.ty_yak.reports.service;

import com.ty_yak.reports.mapper.ReportMapper;
import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.dto.reports.MostActiveUsersDto;
import com.ty_yak.reports.model.dto.reports.ReportResponseDto;
import com.ty_yak.reports.model.enums.ReportType;
import com.ty_yak.reports.repository.ReportDao;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {

    ReportDao reportDao;
    ReportMapper reportMapper;

    public ReportResponseDto getReportData(ReportType reportType) {

        switch (reportType) {

            case MOST_ACTIVE_USERS:

                var activeUsers = reportDao.getMostActiveUsersReportData();

                return new MostActiveUsersDto(activeUsers);
            default:
                throw new IllegalArgumentException("Report type not supported");
        }
    }

    public List<? extends ExportRow> getExportData(ReportType reportType) {

        switch (reportType) {

            case MOST_ACTIVE_USERS:
                return reportMapper.toRowList(reportDao.getMostActiveUsersReportData());
            default:
                throw new IllegalArgumentException("Report type not supported");
        }
    }
}
