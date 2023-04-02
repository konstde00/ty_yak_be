package com.ty_yak.reports.repository;

import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.dto.exports.MostActiveUsersRow;
import com.ty_yak.reports.model.dto.reports.ActiveUserReportDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportDao {

    NamedParameterJdbcTemplate jdbcTemplate;

    public List<ActiveUserReportDto> getMostActiveUsersReportData() {

        String query = """
                
                select 
                    u.username,
                    u.email,
                    u.city,
                    count(us.id) as messages_sent
                from users u
                join users_statuses us on u.id = us.user_id
                group by u.id
                order by messages_sent desc, u.email desc    
                limit 10
                
                """;

        return jdbcTemplate.query(query, (rs, rowNum) -> new ActiveUserReportDto(
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("city"),
                rs.getInt("messages_sent")
        ));
    }
}
