package com.ty_yak.social.repository;

import com.ty_yak.auth.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select exists(select 1 from users_groups where group_id = ?1 and user_id = ?2)",
            nativeQuery = true)
    boolean isUserInGroup(Long groupId, Long userId);

    @Query(value = "select u.email from users u join users_groups ug on u.id = ug.user_id where u.id = ?1",
            nativeQuery = true)
    List<String> getEmailsByUserId(Long userId);

    @Query("update Group g set g.name = ?2 where g.id = ?1")
    void updateName(Long id, String newName);

    @Query(value = "insert into users_groups(group_id, user_id) values (?1, ?2)",
            nativeQuery = true)
    void addUser(Long groupId, Long usersToAdd);

    @Query(value = "delete from users_groups where group_id = ?1 and user_id = ?2",
            nativeQuery = true)
    void removeUserFromGroup(Long groupId, Long userToRemove);
}
