package com.limit.service.limit.repository;

import com.limit.service.limit.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Calendar;

@Repository
public interface ILimitRepository extends JpaRepository<Limit, String> {

    Limit findByActive(boolean active);

    @Transactional
    @Modifying
    @Query("update Limit l set l.active = :active, l.endedAt = :endedAt WHERE l.id = :id")
    void setLimitActive(@Param("id") String id, @Param("active") boolean active, @Param("endedAt") Calendar endedAt);
}
