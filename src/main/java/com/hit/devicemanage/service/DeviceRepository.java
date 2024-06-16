package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    @Query("SELECT e from Device e where e.dgroup = :dgroup or e.dprivi = 0")
    List<Device> findByDgroup(@Param("dgroup") Integer dgroup);
    List<Device> findByTmpgid(int groupId);
}