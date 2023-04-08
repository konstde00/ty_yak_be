package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatusRepository extends JpaRepository<Status, Long> {

    @Query(value = """

            select s.name
            from users_statuses us
                left join statuses s on us.status_id = s.id
            where us.user_id = 1
            order by us.created_at desc
            limit 1
            
            """,
            nativeQuery = true)
    String getStatusByUserId(Long userId);
}
