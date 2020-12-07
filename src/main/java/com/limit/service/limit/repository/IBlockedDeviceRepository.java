package com.limit.service.limit.repository;

import com.limit.service.limit.model.BlockedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IBlockedDeviceRepository extends JpaRepository<BlockedDevice, String> {
    List<BlockedDevice> findByActive(boolean active);

    @Transactional
    @Modifying
    @Query("update BlockedDevice l set l.active = :active WHERE l.messageId = :messageId AND l.deviceId = :deviceId AND l.limitId = :limitId")
    void setLimitActive(@Param("messageId") String messageId, @Param("deviceId") String deviceId, @Param("limitId") String limitId, @Param("active") boolean active);
}
