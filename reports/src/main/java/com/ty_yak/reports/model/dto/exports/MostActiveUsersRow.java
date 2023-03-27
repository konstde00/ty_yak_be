package com.ty_yak.reports.model.dto.exports;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class MostActiveUsersRow implements ExportRow {

    String username;
    String email;
    String city;
    Integer messagesSent;

    @Override
    public List<Object> getExportRow() {
        return List.of(
                Objects.requireNonNullElse(username, StringUtils.EMPTY),
                Objects.requireNonNullElse(email, StringUtils.EMPTY),
                Objects.requireNonNullElse(city, StringUtils.EMPTY),
                Objects.requireNonNullElse(String.valueOf(messagesSent), StringUtils.EMPTY));
    }
}
