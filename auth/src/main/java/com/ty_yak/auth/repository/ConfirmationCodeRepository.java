package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    @Query("select cc from ConfirmationCode cc " +
            "where cc.user.email = :email " +
            "and cc.code = :code " +
            "order by cc.expiredAt desc ")
    List<ConfirmationCode> findAllByEmailAndCode(String email, int code);
}
