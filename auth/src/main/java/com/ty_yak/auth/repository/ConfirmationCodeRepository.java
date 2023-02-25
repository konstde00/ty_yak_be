package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    @Query("select cc from ConfirmationCode cc " +
            "where cc.user.email = :email " +
            "and cc.code = :code")
    Optional<ConfirmationCode> findByEmailAndCode(String email, int code);

    @Query("select cc.user from ConfirmationCode cc " +
            "where cc.user.id =:userId " +
            "and cc.code =:code")
    @EntityGraph(attributePaths = {"roles"})
    Optional<ConfirmationCode> findByIdAndConfirmationCode(Long userId, int code);
}
