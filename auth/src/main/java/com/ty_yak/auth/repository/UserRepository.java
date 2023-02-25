package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    @Query("select cc.user from ConfirmationCode cc " +
            "where cc.user.id =:userId " +
            "and cc.code =:code")
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByIdAndConfirmationCode(Long userId, int code);

    @Modifying
    @Query("delete from User u " +
            "where u.id =:userId")
    void deleteUserById(@Param("userId") Long userId);
}
